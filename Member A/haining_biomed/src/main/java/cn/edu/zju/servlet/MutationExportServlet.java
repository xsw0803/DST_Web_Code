package cn.edu.zju.servlet;

import cn.edu.zju.bean.AnnovarVariant;
import cn.edu.zju.bean.MutationDrugCard;
import cn.edu.zju.bean.MutationMatchResult;
import cn.edu.zju.dao.AnnovarDao;
import cn.edu.zju.dao.MutationMatchingDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "MutationExportServlet", urlPatterns = "/mutationExport")
public class MutationExportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer sampleId = parseSampleId(request.getParameter("sampleId"));
        if (sampleId == null || sampleId <= 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid sample ID");
            return;
        }

        String format = request.getParameter("format");
        String normalizedFormat = format == null ? "csv" : format.trim().toLowerCase();

        AnnovarDao annovarDao = new AnnovarDao();
        List<AnnovarVariant> allVariants = annovarDao.findBySampleId(sampleId);
        if (allVariants.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No variants found for sample ID " + sampleId);
            return;
        }

        List<AnnovarVariant> functionalVariants = new ArrayList<>();
        for (AnnovarVariant variant : allVariants) {
            if (AnnovarDao.isPotentiallyFunctional(variant)) {
                functionalVariants.add(variant);
            }
        }

        MutationMatchingDao mutationMatchingDao = new MutationMatchingDao();
        List<MutationMatchResult> matches = mutationMatchingDao.findMatchesWithFallback(functionalVariants);

        if ("txt".equals(normalizedFormat)) {
            exportSummaryTxt(response, sampleId, allVariants, functionalVariants, matches);
            return;
        }
        exportCsv(response, sampleId, functionalVariants, matches);
    }

    private void exportCsv(HttpServletResponse response, int sampleId, List<AnnovarVariant> functionalVariants, List<MutationMatchResult> matches) throws IOException {
        String filename = "mutation_matching_S" + String.format("%05d", sampleId) + "_professional.csv";
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8))) {
            writer.println("Sample ID,Gene,Variant Name,rsID,Function Status,Phenotype,Matched Drug,Guideline Source,Guideline Summary,Matching Level,Variant Interpretation");
            for (MutationMatchResult match : matches) {
                AnnovarVariant variant = findRepresentativeVariantByGene(functionalVariants, match.getGene());
                String rsId = valueOrEmpty(match.getRsId());
                if (rsId.isEmpty() && variant != null) {
                    rsId = valueOrEmpty(variant.getAvsnp150());
                }
                String functionStatus = valueOrEmpty(match.getFunctionStatus());
                if (functionStatus.isEmpty() && variant != null) {
                    functionStatus = valueOrEmpty(variant.getExonicFuncRefGene());
                }
                writer.println(joinCsv(
                        "S" + String.format("%05d", sampleId),
                        valueOrEmpty(match.getGene()),
                        valueOrEmpty(match.getVariantName()),
                        rsId,
                        functionStatus,
                        valueOrEmpty(match.getPhenotype()),
                        valueOrEmpty(match.getDrugName()),
                        valueOrEmpty(match.getSource()),
                        valueOrEmpty(match.getGuidelineSummary()),
                        valueOrEmpty(match.getMatchingBasis()),
                        valueOrEmpty(match.getVariantInterpretation())
                ));
            }
        }
    }

    private void exportSummaryTxt(HttpServletResponse response, int sampleId, List<AnnovarVariant> allVariants, List<AnnovarVariant> functionalVariants, List<MutationMatchResult> matches) throws IOException {
        String filename = "mutation_matching_S" + String.format("%05d", sampleId) + "_summary.txt";
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/plain; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        List<MutationDrugCard> cards = buildSimplifiedCards(matches);
        Set<String> matchedGenes = new LinkedHashSet<>();
        Set<String> matchedDrugs = new LinkedHashSet<>();
        for (MutationMatchResult match : matches) {
            matchedGenes.add(match.getGene());
            if (match.getDrugName() != null && !match.getDrugName().trim().isEmpty()) {
                matchedDrugs.add(match.getDrugName());
            }
        }

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8))) {
            writer.println("Pharmacogenetic Matching Report");
            writer.println("Generated at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("Sample ID: S" + String.format("%05d", sampleId));
            writer.println();
            writer.println("Summary:");
            writer.println("Total variants uploaded: " + allVariants.size());
            writer.println("Potential functional variants: " + functionalVariants.size());
            writer.println("Matched pharmacogenetic genes: " + matchedGenes.size());
            writer.println("Matched drugs: " + matchedDrugs.size());
            writer.println("Matching method: " + resolveMatchingMethod(matches));
            writer.println();
            writer.println("Matched drugs:");
            int idx = 1;
            for (MutationDrugCard card : cards) {
                writer.println(idx + ". " + valueOrEmpty(card.getDrugName()) + " (" + valueOrEmpty(card.getDrugId()) + ")");
                writer.println("Related genes: " + valueOrEmpty(card.getRelatedGenes()));
                writer.println("Possible implication: " + valueOrEmpty(card.getPossibleImplication()));
                writer.println("Guideline available: " + (card.isGuidelineAvailable() ? "Yes" : "No"));
                writer.println("Advice: " + valueOrEmpty(card.getAdvice()));
                writer.println();
                idx++;
            }
            writer.println("Disclaimer:");
            writer.println("This report is for educational and research reference only.");
            writer.println("It does not provide diagnosis or medical advice.");
            writer.println("Medication decisions should be made by qualified healthcare professionals.");
            writer.println();
            writer.println("Data Sources:");
            writer.println("- PharmGKB-based drug / label / dosing guideline data in this system");
            writer.println("- User-uploaded ANNOVAR-style annotation file");
        }
    }

    private List<MutationDrugCard> buildSimplifiedCards(List<MutationMatchResult> matches) {
        Map<String, MutationDrugCard> cardMap = new LinkedHashMap<>();
        Map<String, Set<String>> genesByDrug = new LinkedHashMap<>();

        for (MutationMatchResult match : matches) {
            String key = buildDrugKey(match.getDrugId(), match.getDrugName());
            MutationDrugCard card = cardMap.computeIfAbsent(key, k -> {
                MutationDrugCard c = new MutationDrugCard();
                c.setDrugId(match.getDrugId());
                if (match.getDrugName() == null || match.getDrugName().trim().isEmpty()) {
                    c.setDrugName((match.getDrugId() == null || match.getDrugId().trim().isEmpty())
                            ? "Unresolved drug record"
                            : "Unresolved drug record (" + match.getDrugId() + ")");
                } else {
                    c.setDrugName(match.getDrugName());
                }
                c.setGuidelineAvailable(match.getGuidelineId() != null && !match.getGuidelineId().trim().isEmpty());
                c.setPossibleImplication("Variants in these genes may affect drug metabolism, treatment response, or toxicity risk.");
                c.setAdvice("Please consult a clinician or pharmacist before changing medication.");
                return c;
            });
            card.setGuidelineAvailable(card.isGuidelineAvailable() || (match.getGuidelineId() != null && !match.getGuidelineId().trim().isEmpty()));
            if (match.getGene() != null && !match.getGene().trim().isEmpty()) {
                genesByDrug.computeIfAbsent(key, k -> new LinkedHashSet<>()).add(match.getGene().trim());
            }
        }

        for (Map.Entry<String, MutationDrugCard> entry : cardMap.entrySet()) {
            Set<String> genes = genesByDrug.get(entry.getKey());
            entry.getValue().setRelatedGenes((genes == null || genes.isEmpty()) ? "N/A" : String.join(", ", genes));
        }
        return new ArrayList<>(cardMap.values());
    }

    private AnnovarVariant findRepresentativeVariantByGene(List<AnnovarVariant> variants, String gene) {
        if (gene == null || gene.trim().isEmpty()) {
            return null;
        }
        for (AnnovarVariant variant : variants) {
            String geneField = variant.getGeneRefGene();
            if (geneField == null || geneField.trim().isEmpty()) {
                continue;
            }
            String[] split = geneField.split("[,;]");
            for (String token : split) {
                if (gene.equals(token.trim())) {
                    return variant;
                }
            }
        }
        return null;
    }

    private String joinCsv(String... fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append('"')
                    .append(valueOrEmpty(fields[i]).replace("\"", "\"\""))
                    .append('"');
        }
        return sb.toString();
    }

    private String buildDrugKey(String drugId, String drugName) {
        if (drugId != null && !drugId.trim().isEmpty()) {
            return drugId.trim();
        }
        return "NO_ID_" + (drugName == null ? "UNKNOWN" : drugName.trim());
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

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private String resolveMatchingMethod(List<MutationMatchResult> matches) {
        for (MutationMatchResult match : matches) {
            if (match.getMatchingBasis() != null && match.getMatchingBasis().toLowerCase().contains("variant-level")) {
                return "Variant-level matching + Gene-level fallback";
            }
        }
        return "Gene-level matching";
    }
}
