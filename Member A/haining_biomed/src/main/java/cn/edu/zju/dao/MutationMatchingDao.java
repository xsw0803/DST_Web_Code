package cn.edu.zju.dao;

import cn.edu.zju.bean.AnnovarVariant;
import cn.edu.zju.bean.MutationMatchResult;
import cn.edu.zju.dbutils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MutationMatchingDao {

    private static final Logger log = LoggerFactory.getLogger(MutationMatchingDao.class);

    public List<MutationMatchResult> findMatchesWithFallback(List<AnnovarVariant> functionalVariants) {
        List<MutationMatchResult> variantLevelMatches = findVariantLevelMatches(functionalVariants);

        Set<String> matchedGenesByVariantLevel = new HashSet<>();
        for (MutationMatchResult match : variantLevelMatches) {
            if (match.getGene() != null && !match.getGene().trim().isEmpty()) {
                matchedGenesByVariantLevel.add(match.getGene().trim());
            }
        }

        Set<String> functionalGenes = extractGenes(functionalVariants);
        functionalGenes.removeAll(matchedGenesByVariantLevel);
        List<MutationMatchResult> geneFallbackMatches = findGuidelineMatchesByGenes(functionalGenes);

        List<MutationMatchResult> merged = new ArrayList<>();
        merged.addAll(variantLevelMatches);
        merged.addAll(geneFallbackMatches);
        return dedupeMatches(merged);
    }

    private List<MutationMatchResult> findVariantLevelMatches(List<AnnovarVariant> functionalVariants) {
        List<MutationMatchResult> results = new ArrayList<>();
        if (functionalVariants == null || functionalVariants.isEmpty()) {
            return results;
        }

        Set<String> rsIds = new LinkedHashSet<>();
        Map<String, AnnovarVariant> representativeByRsId = new HashMap<>();
        for (AnnovarVariant variant : functionalVariants) {
            String rsId = normalizeRsId(variant.getAvsnp150());
            if (rsId == null) {
                continue;
            }
            rsIds.add(rsId);
            representativeByRsId.putIfAbsent(rsId, variant);
        }
        if (rsIds.isEmpty()) {
            return results;
        }

        List<String> idList = new ArrayList<>(rsIds);
        String placeholders = String.join(",", java.util.Collections.nCopies(idList.size(), "?"));
        String sql = "select pv.gene as pv_gene, pv.variant_name, pv.rs_id, pv.function_status, pv.phenotype, pv.evidence_source, pv.interpretation, "
                + "pv.drug_id, d.name as drug_name, dg.id as guideline_id, dg.name as guideline_name, dg.recommendation, dg.source, dg.summary_markdown "
                + "from pgx_variant pv "
                + "left join drug d on pv.drug_id = d.id "
                + "left join dosing_guideline dg on dg.drug_id = pv.drug_id "
                + "where pv.rs_id in (" + placeholders + ")";

        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                for (int i = 0; i < idList.size(); i++) {
                    preparedStatement.setString(i + 1, idList.get(i));
                }
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    MutationMatchResult matchResult = new MutationMatchResult();
                    matchResult.setGene(resultSet.getString("pv_gene"));
                    matchResult.setVariantName(resultSet.getString("variant_name"));
                    matchResult.setRsId(resultSet.getString("rs_id"));
                    matchResult.setFunctionStatus(resultSet.getString("function_status"));
                    matchResult.setPhenotype(resultSet.getString("phenotype"));
                    String interpretation = resultSet.getString("interpretation");
                    if (interpretation == null || interpretation.trim().isEmpty()) {
                        interpretation = "Variant-level pharmacogenetic evidence found by rsID matching.";
                    }
                    matchResult.setVariantInterpretation(interpretation);
                    matchResult.setGuidelineId(resultSet.getString("guideline_id"));
                    matchResult.setGuidelineName(resultSet.getString("guideline_name"));
                    matchResult.setRecommendationAvailable(resultSet.getBoolean("recommendation"));
                    matchResult.setSource(resultSet.getString("source"));
                    matchResult.setGuidelineSummary(resultSet.getString("summary_markdown"));
                    matchResult.setDrugId(resultSet.getString("drug_id"));
                    matchResult.setDrugName(resultSet.getString("drug_name"));
                    matchResult.setMatchingBasis("Variant-level match");
                    results.add(matchResult);
                }
            } catch (SQLException e) {
                log.warn("Variant-level matching skipped due to query issue: {}", e.getMessage());
            }
        });

        if (results.isEmpty()) {
            return results;
        }

        List<MutationMatchResult> filtered = new ArrayList<>();
        for (MutationMatchResult result : results) {
            if (result.getGuidelineName() == null || result.getGuidelineName().trim().isEmpty()) {
                filtered.add(result);
                continue;
            }
            String gene = result.getGene() == null ? "" : result.getGene().trim();
            if (gene.isEmpty()) {
                filtered.add(result);
                continue;
            }
            String guidelineName = result.getGuidelineName() == null ? "" : result.getGuidelineName();
            String guidelineSummary = result.getGuidelineSummary() == null ? "" : result.getGuidelineSummary();
            if (containsIgnoreCase(guidelineName, gene) || containsIgnoreCase(guidelineSummary, gene)) {
                filtered.add(result);
                continue;
            }

            AnnovarVariant representative = representativeByRsId.get(result.getRsId());
            if (representative != null && representative.getGeneRefGene() != null) {
                String[] split = representative.getGeneRefGene().split("[,;]");
                boolean keep = false;
                for (String token : split) {
                    String normalized = token.trim();
                    if (!normalized.isEmpty() && (containsIgnoreCase(guidelineName, normalized) || containsIgnoreCase(guidelineSummary, normalized))) {
                        keep = true;
                        if (result.getGene() == null || result.getGene().trim().isEmpty()) {
                            result.setGene(normalized);
                        }
                        break;
                    }
                }
                if (keep) {
                    filtered.add(result);
                }
            }
        }
        return dedupeMatches(filtered);
    }

    private List<MutationMatchResult> findGuidelineMatchesByGenes(Set<String> genes) {
        List<MutationMatchResult> results = new ArrayList<>();
        if (genes == null || genes.isEmpty()) {
            return results;
        }

        String sql = "select dg.id as guideline_id, dg.name as guideline_name, dg.recommendation, dg.source, dg.summary_markdown, dg.drug_id, d.name as drug_name "
                + "from dosing_guideline dg "
                + "left join drug d on dg.drug_id = d.id "
                + "where dg.name like ? or dg.summary_markdown like ?";

        Set<String> dedupeKeys = new LinkedHashSet<>();
        DBUtils.execSQL(connection -> {
            for (String gene : genes) {
                if (gene == null || gene.trim().isEmpty()) {
                    continue;
                }
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    String like = "%" + gene.trim() + "%";
                    preparedStatement.setString(1, like);
                    preparedStatement.setString(2, like);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        String guidelineId = resultSet.getString("guideline_id");
                        String drugId = resultSet.getString("drug_id");
                        String key = gene + "|" + guidelineId + "|" + drugId;
                        if (dedupeKeys.contains(key)) {
                            continue;
                        }
                        dedupeKeys.add(key);

                        MutationMatchResult matchResult = new MutationMatchResult();
                        matchResult.setGene(gene);
                        matchResult.setGuidelineId(guidelineId);
                        matchResult.setGuidelineName(resultSet.getString("guideline_name"));
                        matchResult.setRecommendationAvailable(resultSet.getBoolean("recommendation"));
                        matchResult.setSource(resultSet.getString("source"));
                        matchResult.setGuidelineSummary(resultSet.getString("summary_markdown"));
                        matchResult.setDrugId(drugId);
                        matchResult.setDrugName(resultSet.getString("drug_name"));
                        matchResult.setMatchingBasis("Gene-level fallback");
                        matchResult.setVariantInterpretation("Gene-level evidence only. Variant-specific pharmacogenetic record was not matched.");
                        results.add(matchResult);
                    }
                } catch (SQLException e) {
                    log.error("Failed to match pharmacogenetic guidelines by gene {}", gene, e);
                }
            }
        });
        return results;
    }

    private Set<String> extractGenes(List<AnnovarVariant> variants) {
        Set<String> genes = new LinkedHashSet<>();
        if (variants == null) {
            return genes;
        }
        for (AnnovarVariant variant : variants) {
            String geneField = variant.getGeneRefGene();
            if (geneField == null || geneField.trim().isEmpty()) {
                continue;
            }
            String[] split = geneField.split("[,;]");
            for (String token : split) {
                String gene = token.trim();
                if (!gene.isEmpty() && !".".equals(gene)) {
                    genes.add(gene);
                }
            }
        }
        return genes;
    }

    private String normalizeRsId(String raw) {
        if (raw == null) {
            return null;
        }
        String normalized = raw.trim();
        if (normalized.isEmpty() || ".".equals(normalized) || "NA".equalsIgnoreCase(normalized)) {
            return null;
        }
        String[] split = normalized.split("[,;]");
        for (String token : split) {
            String value = token.trim();
            if (!value.isEmpty() && !".".equals(value) && !"NA".equalsIgnoreCase(value)) {
                return value;
            }
        }
        return null;
    }

    private boolean containsIgnoreCase(String full, String sub) {
        if (full == null || sub == null) {
            return false;
        }
        return full.toLowerCase().contains(sub.toLowerCase());
    }

    private List<MutationMatchResult> dedupeMatches(List<MutationMatchResult> matches) {
        Map<String, MutationMatchResult> dedupe = new LinkedHashMap<>();
        for (MutationMatchResult match : matches) {
            String key = safe(match.getMatchingBasis()) + "|"
                    + safe(match.getGene()) + "|"
                    + safe(match.getRsId()) + "|"
                    + safe(match.getDrugId()) + "|"
                    + safe(match.getGuidelineId());
            dedupe.putIfAbsent(key, match);
        }
        return new ArrayList<>(dedupe.values());
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
