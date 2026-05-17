package cn.edu.zju.bean;

public class MutationSourceEvidenceSummary {
    private String drugId;
    private String drugName;
    private int labelRecordCount;
    private int guidelineRecordCount;
    private String mainLabelSources;
    private boolean viewEvidenceEnabled;
    private boolean unresolvedDrugRecord;

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

    public int getLabelRecordCount() {
        return labelRecordCount;
    }

    public void setLabelRecordCount(int labelRecordCount) {
        this.labelRecordCount = labelRecordCount;
    }

    public int getGuidelineRecordCount() {
        return guidelineRecordCount;
    }

    public void setGuidelineRecordCount(int guidelineRecordCount) {
        this.guidelineRecordCount = guidelineRecordCount;
    }

    public String getMainLabelSources() {
        return mainLabelSources;
    }

    public void setMainLabelSources(String mainLabelSources) {
        this.mainLabelSources = mainLabelSources;
    }

    public boolean isViewEvidenceEnabled() {
        return viewEvidenceEnabled;
    }

    public void setViewEvidenceEnabled(boolean viewEvidenceEnabled) {
        this.viewEvidenceEnabled = viewEvidenceEnabled;
    }

    public boolean isUnresolvedDrugRecord() {
        return unresolvedDrugRecord;
    }

    public void setUnresolvedDrugRecord(boolean unresolvedDrugRecord) {
        this.unresolvedDrugRecord = unresolvedDrugRecord;
    }
}
