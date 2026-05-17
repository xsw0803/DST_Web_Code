package cn.edu.zju.bean;

public class MutationSampleSummary {
    private String sampleId;
    private int totalVariants;
    private int functionalVariantCount;
    private int matchedGeneCount;
    private int matchedDrugCount;
    private String matchingMethod;

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public int getTotalVariants() {
        return totalVariants;
    }

    public void setTotalVariants(int totalVariants) {
        this.totalVariants = totalVariants;
    }

    public int getFunctionalVariantCount() {
        return functionalVariantCount;
    }

    public void setFunctionalVariantCount(int functionalVariantCount) {
        this.functionalVariantCount = functionalVariantCount;
    }

    public int getMatchedGeneCount() {
        return matchedGeneCount;
    }

    public void setMatchedGeneCount(int matchedGeneCount) {
        this.matchedGeneCount = matchedGeneCount;
    }

    public int getMatchedDrugCount() {
        return matchedDrugCount;
    }

    public void setMatchedDrugCount(int matchedDrugCount) {
        this.matchedDrugCount = matchedDrugCount;
    }

    public String getMatchingMethod() {
        return matchingMethod;
    }

    public void setMatchingMethod(String matchingMethod) {
        this.matchingMethod = matchingMethod;
    }
}
