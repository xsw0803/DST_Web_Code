package cn.edu.zju.service;

import cn.edu.zju.bean.DosingGuideline;
import cn.edu.zju.bean.Drug;
import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.bean.Sample;

import java.util.List;

public interface ExplanationService {

    String explainDrug(Drug drug, int relatedLabelCount, int relatedGuidelineCount);

    String explainDrugLabel(DrugLabel drugLabel);

    String explainDosingGuideline(DosingGuideline dosingGuideline);

    String explainSampleResult(Sample sample, List<String> refGenes, List<DrugLabel> matchedLabels);
}
