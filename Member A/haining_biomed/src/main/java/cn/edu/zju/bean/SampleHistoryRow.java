package cn.edu.zju.bean;

import java.util.Date;

public class SampleHistoryRow {
    private int sampleId;
    private String fileName;
    private Date uploadTime;
    private String uploadedBy;
    private int variantCount;
    private int matchedGeneCount;
    private int matchedDrugCount;
    private String status;

    public int getSampleId() {
        return sampleId;
    }

    public void setSampleId(int sampleId) {
        this.sampleId = sampleId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public int getVariantCount() {
        return variantCount;
    }

    public void setVariantCount(int variantCount) {
        this.variantCount = variantCount;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
