package cn.edu.zju.bean;

public class MutationMatchResult {
    private String gene;
    private String guidelineId;
    private String guidelineName;
    private boolean recommendationAvailable;
    private String source;
    private String guidelineSummary;
    private String drugId;
    private String drugName;
    private String matchingBasis;
    private String rsId;
    private String variantName;
    private String functionStatus;
    private String phenotype;
    private String variantInterpretation;

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getGuidelineId() {
        return guidelineId;
    }

    public void setGuidelineId(String guidelineId) {
        this.guidelineId = guidelineId;
    }

    public String getGuidelineName() {
        return guidelineName;
    }

    public void setGuidelineName(String guidelineName) {
        this.guidelineName = guidelineName;
    }

    public boolean isRecommendationAvailable() {
        return recommendationAvailable;
    }

    public void setRecommendationAvailable(boolean recommendationAvailable) {
        this.recommendationAvailable = recommendationAvailable;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getGuidelineSummary() {
        return guidelineSummary;
    }

    public void setGuidelineSummary(String guidelineSummary) {
        this.guidelineSummary = guidelineSummary;
    }

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

    public String getMatchingBasis() {
        return matchingBasis;
    }

    public void setMatchingBasis(String matchingBasis) {
        this.matchingBasis = matchingBasis;
    }

    public String getRsId() {
        return rsId;
    }

    public void setRsId(String rsId) {
        this.rsId = rsId;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getFunctionStatus() {
        return functionStatus;
    }

    public void setFunctionStatus(String functionStatus) {
        this.functionStatus = functionStatus;
    }

    public String getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(String phenotype) {
        this.phenotype = phenotype;
    }

    public String getVariantInterpretation() {
        return variantInterpretation;
    }

    public void setVariantInterpretation(String variantInterpretation) {
        this.variantInterpretation = variantInterpretation;
    }
}
