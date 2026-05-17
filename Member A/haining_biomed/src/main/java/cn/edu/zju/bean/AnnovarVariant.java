package cn.edu.zju.bean;

public class AnnovarVariant {
    private int sampleId;
    private String chr;
    private String start;
    private String end;
    private String ref;
    private String alt;
    private String funcRefGene;
    private String geneRefGene;
    private String exonicFuncRefGene;
    private String aaChangeRefGene;
    private String avsnp150;
    private boolean matched;
    private String functionalStatus;

    public int getSampleId() {
        return sampleId;
    }

    public void setSampleId(int sampleId) {
        this.sampleId = sampleId;
    }

    public String getChr() {
        return chr;
    }

    public void setChr(String chr) {
        this.chr = chr;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getFuncRefGene() {
        return funcRefGene;
    }

    public void setFuncRefGene(String funcRefGene) {
        this.funcRefGene = funcRefGene;
    }

    public String getGeneRefGene() {
        return geneRefGene;
    }

    public void setGeneRefGene(String geneRefGene) {
        this.geneRefGene = geneRefGene;
    }

    public String getExonicFuncRefGene() {
        return exonicFuncRefGene;
    }

    public void setExonicFuncRefGene(String exonicFuncRefGene) {
        this.exonicFuncRefGene = exonicFuncRefGene;
    }

    public String getAaChangeRefGene() {
        return aaChangeRefGene;
    }

    public void setAaChangeRefGene(String aaChangeRefGene) {
        this.aaChangeRefGene = aaChangeRefGene;
    }

    public String getAvsnp150() {
        return avsnp150;
    }

    public void setAvsnp150(String avsnp150) {
        this.avsnp150 = avsnp150;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public String getFunctionalStatus() {
        return functionalStatus;
    }

    public void setFunctionalStatus(String functionalStatus) {
        this.functionalStatus = functionalStatus;
    }
}
