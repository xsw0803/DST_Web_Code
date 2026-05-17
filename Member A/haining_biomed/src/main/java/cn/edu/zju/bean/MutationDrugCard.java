package cn.edu.zju.bean;

public class MutationDrugCard {
    private String drugId;
    private String drugName;
    private String relatedGenes;
    private boolean guidelineAvailable;
    private String matchingLevel;
    private String evidenceBasis;
    private String possibleImplication;
    private String advice;
    private boolean unresolvedDrugRecord;
    private boolean viewEvidenceEnabled;

    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getRelatedGenes() {
        return relatedGenes;
    }

    public void setRelatedGenes(String relatedGenes) {
        this.relatedGenes = relatedGenes;
    }

    public boolean isGuidelineAvailable() {
        return guidelineAvailable;
    }

    public void setGuidelineAvailable(boolean guidelineAvailable) {
        this.guidelineAvailable = guidelineAvailable;
    }

    public String getMatchingLevel() {
        return matchingLevel;
    }

    public void setMatchingLevel(String matchingLevel) {
        this.matchingLevel = matchingLevel;
    }

    public String getEvidenceBasis() {
        return evidenceBasis;
    }

    public void setEvidenceBasis(String evidenceBasis) {
        this.evidenceBasis = evidenceBasis;
    }

    public String getPossibleImplication() {
        return possibleImplication;
    }

    public void setPossibleImplication(String possibleImplication) {
        this.possibleImplication = possibleImplication;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public boolean isUnresolvedDrugRecord() {
        return unresolvedDrugRecord;
    }

    public void setUnresolvedDrugRecord(boolean unresolvedDrugRecord) {
        this.unresolvedDrugRecord = unresolvedDrugRecord;
    }

    public boolean isViewEvidenceEnabled() {
        return viewEvidenceEnabled;
    }

    public void setViewEvidenceEnabled(boolean viewEvidenceEnabled) {
        this.viewEvidenceEnabled = viewEvidenceEnabled;
    }
}
