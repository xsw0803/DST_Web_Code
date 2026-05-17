package cn.edu.zju.servlet;

import cn.edu.zju.bean.AnnovarVariant;
import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.bean.MutationDrugCard;
import cn.edu.zju.bean.MutationLabelEvidence;
import cn.edu.zju.bean.MutationMatchResult;
import cn.edu.zju.bean.MutationSampleSummary;
import cn.edu.zju.bean.MutationSourceEvidenceSummary;
import cn.edu.zju.dao.AnnovarDao;
import cn.edu.zju.dao.DrugLabelDao;
import cn.edu.zju.dao.MutationMatchingDao;
import cn.edu.zju.filter.AuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "MutationResultServlet", urlPatterns = "/mutationResult")
public class MutationResultServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currentRole = (String) request.getSession().getAttribute(AuthenticationFilter.SESSION_ROLE);
        boolean canViewProfessional = AuthenticationFilter.ROLE_PROFESSIONAL.equals(currentRole)
                || AuthenticationFilter.ROLE_ADMIN.equals(currentRole);
        boolean canViewSampleHistory = AuthenticationFilter.ROLE_ADMIN.equals(currentRole);

        Integer sampleId = parseSampleId(request.getParameter("sampleId"));
        if (sampleId == null || sampleId <= 0) {
            request.setAttribute("error", "Invalid sample ID.");
            request.setAttribute("canViewProfessional", canViewProfessional);
            request.setAttribute("canViewSampleHistory", canViewSampleHistory);
            request.getRequestDispatcher("/views/mutation_result.jsp").forward(request, response);
            return;
        }

        AnnovarDao annovarDao = new AnnovarDao();
        List<AnnovarVariant> allVariants = annovarDao.findBySampleId(sampleId);
        if (allVariants.isEmpty()) {
            request.setAttribute("error", "No variants found for sample ID: " + sampleId);
            request.setAttribute("sampleId", sampleId);
            request.setAttribute("canViewProfessional", canViewProfessional);
            request.setAttribute("canViewSampleHistory", canViewSampleHistory);
            request.getRequestDispatcher("/views/mutation_result.jsp").forward(request, response);
            return;
        }

        List<AnnovarVariant> functionalVariants = new ArrayList<>();
        for (AnnovarVariant variant : allVariants) {
            variant.setFunctionalStatus(classifyFunctionalStatus(variant));
            if (AnnovarDao.isPotentiallyFunctional(variant)) {
                functionalVariants.add(variant);
            }
        }

        MutationMatchingDao mutationMatchingDao = new MutationMatchingDao();
        List<MutationMatchResult> matches = mutationMatchingDao.findMatchesWithFallback(functionalVariants);

        Set<String> matchedGenes = new LinkedHashSet<>();
        Set<String> matchedDrugIds = new LinkedHashSet<>();
        boolean hasVariantLevelMatch = false;
        for (MutationMatchResult match : matches) {
            if (match.getGene() != null && !match.getGene().trim().isEmpty()) {
                matchedGenes.add(match.getGene().trim());
            }
            if (match.getDrugId() != null && !match.getDrugId().trim().isEmpty()) {
                matchedDrugIds.add(match.getDrugId());
            }
            if ("Variant-level match".equals(match.getMatchingBasis())) {
                hasVariantLevelMatch = true;
            }
        }

        Set<String> matchedGeneTokens = new HashSet<>(matchedGenes);
        for (AnnovarVariant variant : allVariants) {
            variant.setMatched(isVariantMatched(variant, matchedGeneTokens));
        }

        MutationSampleSummary sampleSummary = new MutationSampleSummary();
        sampleSummary.setSampleId(String.format("S%05d", sampleId));
        sampleSummary.setTotalVariants(allVariants.size());
        sampleSummary.setFunctionalVariantCount(functionalVariants.size());
        sampleSummary.setMatchedGeneCount(matchedGenes.size());
        sampleSummary.setMatchedDrugCount(matchedDrugIds.size());
        sampleSummary.setMatchingMethod(hasVariantLevelMatch ? "Variant-level matching + Gene-level fallback" : "Gene-level matching");

        List<MutationDrugCard> simplifiedCards = buildSimplifiedCards(matches);
        List<MutationLabelEvidence> labelEvidenceList = buildLabelEvidence(matches, matchedDrugIds);
        List<MutationSourceEvidenceSummary> sourceEvidenceSummaries = buildSourceEvidenceSummaries(simplifiedCards, matches, labelEvidenceList);

        request.setAttribute("sampleId", sampleId);
        request.setAttribute("sampleSummary", sampleSummary);
        request.setAttribute("simplifiedCards", simplifiedCards);
        request.setAttribute("allVariants", allVariants);
        request.setAttribute("functionalVariants", functionalVariants);
        request.setAttribute("matchedResults", matches);
        request.setAttribute("labelEvidenceList", labelEvidenceList);
        request.setAttribute("sourceEvidenceSummaries", sourceEvidenceSummaries);
        request.setAttribute("canViewProfessional", canViewProfessional);
        request.setAttribute("canViewSampleHistory", canViewSampleHistory);
        request.getRequestDispatcher("/views/mutation_result.jsp").forward(request, response);
    }

    private Integer parseSampleId(String sampleId) {
        if (sampleId == null || sampleId.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(sampleId.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean isVariantMatched(AnnovarVariant variant, Set<String> matchedGenes) {
        if (variant.getGeneRefGene() == null || variant.getGeneRefGene().trim().isEmpty()) {
            return false;
        }
        String[] split = variant.getGeneRefGene().split("[,;]");
        for (String token : split) {
            if (matchedGenes.contains(token.trim())) {
                return true;
            }
        }
        return false;
    }

    private String classifyFunctionalStatus(AnnovarVariant variant) {
        if (AnnovarDao.isPotentiallyFunctional(variant)) {
            String exonicFunc = variant.getExonicFuncRefGene() == null ? "" : variant.getExonicFuncRefGene().toLowerCase();
            if (exonicFunc.contains("nonsynonymous")
                    || exonicFunc.contains("stopgain")
                    || exonicFunc.contains("stoploss")
                    || exonicFunc.contains("frameshift")) {
                return "Functional";
            }
            return "Possibly functional";
        }
        return "Likely non-functional";
    }

    private List<MutationDrugCard> buildSimplifiedCards(List<MutationMatchResult> matches) {
        Map<String, MutationDrugCard> cardMap = new LinkedHashMap<>();
        Map<String, Set<String>> genesByDrug = new LinkedHashMap<>();
        Map<String, Set<String>> rsByDrug = new LinkedHashMap<>();
        Map<String, Set<String>> matchingBasisByDrug = new LinkedHashMap<>();

        for (MutationMatchResult match : matches) {
            String key = buildDrugKey(match.getDrugId(), match.getDrugName());
            MutationDrugCard card = cardMap.computeIfAbsent(key, k -> {
                MutationDrugCard c = new MutationDrugCard();
                String drugId = match.getDrugId();
                String drugName = match.getDrugName();
                boolean unresolved = (drugName == null || drugName.trim().isEmpty());
                c.setDrugId(drugId);
                c.setUnresolvedDrugRecord(unresolved);
                if (unresolved) {
                    c.setDrugName((drugId == null || drugId.trim().isEmpty()) ? "Unresolved drug record" : "Unresolved drug record (" + drugId + ")");
                } else {
                    c.setDrugName(drugName);
                }
                c.setGuidelineAvailable(match.getGuidelineId() != null && !match.getGuidelineId().trim().isEmpty());
                c.setViewEvidenceEnabled(!unresolved && drugId != null && !drugId.trim().isEmpty());
                return c;
            });
            card.setGuidelineAvailable(card.isGuidelineAvailable() || (match.getGuidelineId() != null && !match.getGuidelineId().trim().isEmpty()));
            if (match.getGene() != null && !match.getGene().trim().isEmpty()) {
                genesByDrug.computeIfAbsent(key, k -> new LinkedHashSet<>()).add(match.getGene().trim());
            }
            if (match.getRsId() != null && !match.getRsId().trim().isEmpty() && !"-".equals(match.getRsId().trim())) {
                rsByDrug.computeIfAbsent(key, k -> new LinkedHashSet<>()).add(match.getRsId().trim());
            }
            if (match.getMatchingBasis() != null && !match.getMatchingBasis().trim().isEmpty()) {
                matchingBasisByDrug.computeIfAbsent(key, k -> new LinkedHashSet<>()).add(match.getMatchingBasis().trim());
            }
        }

        for (Map.Entry<String, MutationDrugCard> entry : cardMap.entrySet()) {
            MutationDrugCard card = entry.getValue();
            Set<String> genes = genesByDrug.get(entry.getKey());
            Set<String> rsIds = rsByDrug.get(entry.getKey());
            Set<String> basis = matchingBasisByDrug.get(entry.getKey());

            String geneText = (genes == null || genes.isEmpty()) ? "N/A" : String.join(", ", genes);
            card.setRelatedGenes(geneText);
            card.setMatchingLevel(resolveMatchingLevel(basis));
            card.setEvidenceBasis(buildEvidenceBasis(card, geneText, rsIds));
            card.setPossibleImplication(buildPossibleImplication(genes));
            card.setAdvice(buildAdvice(card.isUnresolvedDrugRecord()));
        }
        return new ArrayList<>(cardMap.values());
    }

    private String resolveMatchingLevel(Set<String> basis) {
        if (basis == null || basis.isEmpty()) {
            return "No direct match";
        }
        for (String item : basis) {
            if (item != null && item.toLowerCase().contains("variant-level")) {
                return "Variant-level match";
            }
        }
        for (String item : basis) {
            if (item != null && item.toLowerCase().contains("gene-level")) {
                return "Gene-level match";
            }
        }
        return "Gene-level match";
    }

    private String buildEvidenceBasis(MutationDrugCard card, String geneText, Set<String> rsIds) {
        if (card.isUnresolvedDrugRecord()) {
            return "A guideline match was found by related gene " + geneText + ", but the linked drug record is unavailable in the current drug table.";
        }
        if ("Variant-level match".equals(card.getMatchingLevel()) && rsIds != null && !rsIds.isEmpty()) {
            return "Matched by rsID " + String.join(", ", rsIds) + " and related gene " + geneText + " in dosing guideline records.";
        }
        return "Matched by related gene " + geneText + " in dosing guideline records.";
    }

    private String buildPossibleImplication(Set<String> genes) {
        if (genes == null || genes.isEmpty()) {
            return "This match may indicate altered drug metabolism, treatment response, or toxicity risk and should be interpreted professionally.";
        }

        String upperGenes = String.join(",", genes).toUpperCase();
        if (upperGenes.contains("DPYD")) {
            return "Variants in DPYD may be related to altered fluoropyrimidine metabolism and toxicity risk.";
        }
        if (upperGenes.contains("TPMT") || upperGenes.contains("NUDT15")) {
            return "Variants in TPMT/NUDT15 may be associated with altered thiopurine metabolism and toxicity risk.";
        }
        if (upperGenes.contains("CYP2C19")) {
            return "Variants in CYP2C19 may affect metabolism and response for CYP2C19-related therapies.";
        }
        if (upperGenes.contains("CYP2D6")) {
            return "Variants in CYP2D6 may be associated with altered metabolism and treatment response for CYP2D6-related drugs.";
        }
        return "A potentially relevant variant was detected in " + String.join(", ", genes) + ", a gene linked to this drug in the current pharmacogenetic guideline database.";
    }

    private String buildAdvice(boolean unresolvedDrugRecord) {
        if (unresolvedDrugRecord) {
            return "Please review the professional evidence table or source records before interpretation.";
        }
        return "This result is for reference only. Please consult a clinician or pharmacist before changing medication.";
    }

    private List<MutationLabelEvidence> buildLabelEvidence(List<MutationMatchResult> matches, Set<String> matchedDrugIds) {
        List<MutationLabelEvidence> evidenceList = new ArrayList<>();
        if (matchedDrugIds.isEmpty()) {
            return evidenceList;
        }

        Map<String, String> drugNameById = new LinkedHashMap<>();
        for (MutationMatchResult match : matches) {
            if (match.getDrugId() != null && !match.getDrugId().trim().isEmpty()) {
                drugNameById.putIfAbsent(match.getDrugId(), match.getDrugName());
            }
        }

        DrugLabelDao drugLabelDao = new DrugLabelDao();
        for (String drugId : matchedDrugIds) {
            List<DrugLabel> labels = drugLabelDao.findByDrugId(drugId);
            for (DrugLabel label : labels) {
                MutationLabelEvidence evidence = new MutationLabelEvidence();
                evidence.setDrugId(drugId);
                String resolvedDrugName = drugNameById.get(drugId);
                if (resolvedDrugName == null || resolvedDrugName.trim().isEmpty()) {
                    evidence.setDrugName("Unresolved drug record");
                } else {
                    evidence.setDrugName(resolvedDrugName);
                }
                evidence.setLabelSource(label.getSource());
                evidence.setDosingInformationAvailable(label.isDosingInformation());
                evidence.setLabelSummary(label.getSummaryMarkdown());
                evidenceList.add(evidence);
            }
        }
        return evidenceList;
    }

    private String buildDrugKey(String drugId, String drugName) {
        if (drugId != null && !drugId.trim().isEmpty()) {
            return drugId.trim();
        }
        return "NO_ID_" + (drugName == null ? "UNKNOWN" : drugName.trim());
    }

    private List<MutationSourceEvidenceSummary> buildSourceEvidenceSummaries(List<MutationDrugCard> cards,
                                                                              List<MutationMatchResult> matches,
                                                                              List<MutationLabelEvidence> labelEvidenceList) {
        Map<String, MutationSourceEvidenceSummary> summaryMap = new LinkedHashMap<>();
        Map<String, Set<String>> guidelineUniqueByDrug = new LinkedHashMap<>();
        Map<String, Set<String>> labelSourceByDrug = new LinkedHashMap<>();

        for (MutationDrugCard card : cards) {
            String key = buildDrugKey(card.getDrugId(), card.getDrugName());
            MutationSourceEvidenceSummary summary = new MutationSourceEvidenceSummary();
            summary.setDrugId(card.getDrugId());
            summary.setDrugName(card.getDrugName());
            summary.setUnresolvedDrugRecord(card.isUnresolvedDrugRecord());
            summary.setViewEvidenceEnabled(card.isViewEvidenceEnabled());
            summary.setLabelRecordCount(0);
            summary.setGuidelineRecordCount(0);
            summary.setMainLabelSources("Not available");
            summaryMap.put(key, summary);
        }

        for (MutationMatchResult match : matches) {
            String key = buildDrugKey(match.getDrugId(), match.getDrugName());
            MutationSourceEvidenceSummary summary = summaryMap.computeIfAbsent(key, k -> {
                MutationSourceEvidenceSummary created = new MutationSourceEvidenceSummary();
                created.setDrugId(match.getDrugId());
                boolean unresolved = match.getDrugName() == null || match.getDrugName().trim().isEmpty();
                created.setDrugName(unresolved
                        ? ((match.getDrugId() == null || match.getDrugId().trim().isEmpty()) ? "Unresolved drug record" : "Unresolved drug record (" + match.getDrugId() + ")")
                        : match.getDrugName());
                created.setUnresolvedDrugRecord(unresolved);
                created.setViewEvidenceEnabled(!unresolved && match.getDrugId() != null && !match.getDrugId().trim().isEmpty());
                created.setMainLabelSources("Not available");
                return created;
            });

            if (match.getGuidelineId() != null && !match.getGuidelineId().trim().isEmpty()) {
                guidelineUniqueByDrug.computeIfAbsent(key, k -> new LinkedHashSet<>()).add(match.getGuidelineId().trim());
            }
            summary.setDrugId(summary.getDrugId() == null ? match.getDrugId() : summary.getDrugId());
        }

        for (MutationLabelEvidence labelEvidence : labelEvidenceList) {
            String key = buildDrugKey(labelEvidence.getDrugId(), labelEvidence.getDrugName());
            MutationSourceEvidenceSummary summary = summaryMap.get(key);
            if (summary == null) {
                continue;
            }
            summary.setLabelRecordCount(summary.getLabelRecordCount() + 1);
            if (labelEvidence.getLabelSource() != null && !labelEvidence.getLabelSource().trim().isEmpty()) {
                labelSourceByDrug.computeIfAbsent(key, k -> new LinkedHashSet<>()).add(labelEvidence.getLabelSource().trim());
            }
        }

        for (Map.Entry<String, MutationSourceEvidenceSummary> entry : summaryMap.entrySet()) {
            String key = entry.getKey();
            MutationSourceEvidenceSummary summary = entry.getValue();
            Set<String> guidelineUnique = guidelineUniqueByDrug.get(key);
            summary.setGuidelineRecordCount(guidelineUnique == null ? 0 : guidelineUnique.size());

            Set<String> labelSources = labelSourceByDrug.get(key);
            if (labelSources != null && !labelSources.isEmpty()) {
                summary.setMainLabelSources(joinTopSources(labelSources, 3));
            }
        }
        return new ArrayList<>(summaryMap.values());
    }

    private String joinTopSources(Set<String> sources, int limit) {
        List<String> topSources = new ArrayList<>();
        int index = 0;
        for (String source : sources) {
            topSources.add(source);
            index++;
            if (index >= limit) {
                break;
            }
        }
        if (sources.size() > limit) {
            topSources.add("...");
        }
        return String.join(", ", topSources);
    }
}
