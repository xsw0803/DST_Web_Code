package cn.edu.zju.service;

import cn.edu.zju.bean.DosingGuideline;
import cn.edu.zju.bean.Drug;
import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.bean.Sample;

import java.util.List;
import java.util.StringJoiner;

public class TemplateExplanationService implements ExplanationService {

    @Override
    public String explainDrug(Drug drug, int relatedLabelCount, int relatedGuidelineCount) {
        String biomarkerText = drug.isBiomarker() ? "is marked as a biomarker-related drug" : "is not marked as a biomarker-specific drug";
        return drug.getName() + " " + biomarkerText + " in the pharmacogenomics knowledge base. " +
                "The current record is connected to " + relatedLabelCount + " drug label item(s) and " +
                relatedGuidelineCount + " dosing guideline item(s), so users can use this page as a starting point for deeper review.";
    }

    @Override
    public String explainDrugLabel(DrugLabel drugLabel) {
        String source = drugLabel.getSource() == null ? "the available source" : drugLabel.getSource();
        String dosingFlag = drugLabel.isDosingInformation()
                ? "It explicitly includes dosing-related information."
                : "It does not appear to be primarily dosing-focused.";
        return "This drug label annotation comes from " + source + " and summarizes pharmacogenomic evidence linked to drug " +
                drugLabel.getDrugId() + ". " + dosingFlag + " Users should read the summary and full text together before interpreting the clinical context.";
    }

    @Override
    public String explainDosingGuideline(DosingGuideline dosingGuideline) {
        String source = dosingGuideline.getSource() == null ? "the available source" : dosingGuideline.getSource();
        String recommendationText = dosingGuideline.isRecommendation()
                ? "It is marked as a recommendation-oriented guideline."
                : "It is presented as supporting guidance rather than a direct recommendation flag.";
        return "This dosing guideline is linked to drug " + dosingGuideline.getDrugId() + " and comes from " + source + ". " +
                recommendationText + " The summary highlights the main dosing implication, while the full text provides the detailed recommendation context.";
    }

    @Override
    public String explainSampleResult(Sample sample, List<String> refGenes, List<DrugLabel> matchedLabels) {
        StringJoiner geneJoiner = new StringJoiner(", ");
        for (int i = 0; i < refGenes.size() && i < 5; i++) {
            geneJoiner.add(refGenes.get(i));
        }

        StringJoiner labelJoiner = new StringJoiner(", ");
        for (int i = 0; i < matchedLabels.size() && i < 3; i++) {
            labelJoiner.add(matchedLabels.get(i).getDisplayName());
        }

        String genesText = refGenes.isEmpty() ? "No non-synonymous genes were detected" :
                "The uploaded sample contains " + refGenes.size() + " non-synonymous reference gene(s), including " + geneJoiner;
        String labelsText = matchedLabels.isEmpty() ? "No matched pharmacogenomics drug labels were returned."
                : "The current workflow matched " + matchedLabels.size() + " drug label record(s). Example matched labels include " + labelJoiner + ".";

        return "Sample #" + sample.getId() + " uploaded by " + sample.getUploadedBy() + " has completed the pharmacogenomics matching workflow. "
                + genesText + ". " + labelsText
                + " Users should review the matched labels to understand which drugs may require attention, dosing review, or label-based interpretation.";
    }
}
