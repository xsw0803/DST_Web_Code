package cn.edu.zju.bean;

public class MutationLabelEvidence {
    private String drugId;
    private String drugName;
    private String labelSource;
    private boolean dosingInformationAvailable;
    private String labelSummary;

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

    public String getLabelSource() {
        return labelSource;
    }

    public void setLabelSource(String labelSource) {
        this.labelSource = labelSource;
    }

    public boolean isDosingInformationAvailable() {
        return dosingInformationAvailable;
    }

    public void setDosingInformationAvailable(boolean dosingInformationAvailable) {
        this.dosingInformationAvailable = dosingInformationAvailable;
    }

    public String getLabelSummary() {
        return labelSummary;
    }

    public void setLabelSummary(String labelSummary) {
        this.labelSummary = labelSummary;
    }
}
