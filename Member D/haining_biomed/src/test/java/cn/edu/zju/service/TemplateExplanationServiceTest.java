package cn.edu.zju.service;

import cn.edu.zju.bean.Drug;
import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.bean.Sample;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TemplateExplanationServiceTest {

    private final TemplateExplanationService service = new TemplateExplanationService();

    @Test
    public void explainDrug_whenDrugIsBiomarker_mentionsRelatedCounts() {
        // Given
        Drug drug = new Drug("PA10026", "aripiprazole", true, "/drug/PA10026", "Chemical");

        // When
        String explanation = service.explainDrug(drug, 4, 1);

        // Then
        assertTrue("Expected explanation to mention the drug name.", explanation.contains("aripiprazole"));
        assertTrue("Expected explanation to mention related label count.", explanation.contains("4 drug label item(s)"));
        assertTrue("Expected explanation to mention related guideline count.", explanation.contains("1 dosing guideline item(s)"));
    }

    @Test
    public void explainSampleResult_whenSampleContainsGenesAndMatches_mentionsGenesAndLabels() {
        // Given
        Sample sample = new Sample(11, new Date(Timestamp.valueOf("2026-05-09 23:06:36").getTime()), "Jiang");
        List<String> refGenes = Collections.singletonList("CYP2D6");
        List<DrugLabel> matchedLabels = Arrays.asList(
                new DrugLabel("PA166104787", "PA166104787", "DrugLabelAnnotation", false, false, "", "U.S. Food and Drug Administration",
                        "", "Summary 1", "{\"name\":\"FDA Label for cevimeline and CYP2D6\"}", "PA10000"),
                new DrugLabel("PA166104788", "PA166104788", "DrugLabelAnnotation", false, false, "", "U.S. Food and Drug Administration",
                        "", "Summary 2", "{\"name\":\"FDA Label for fluoxetine / olanzapine and CYP2D6\"}", "PA10001")
        );

        // When
        String explanation = service.explainSampleResult(sample, refGenes, matchedLabels);

        // Then
        assertTrue("Expected explanation to mention the sample id.", explanation.contains("Sample #11"));
        assertTrue("Expected explanation to mention the detected gene.", explanation.contains("CYP2D6"));
        assertTrue("Expected explanation to mention at least one matched label.", explanation.contains("FDA Label for cevimeline and CYP2D6"));
    }
}
