package cn.edu.zju.dao;

import cn.edu.zju.bean.DosingGuideline;
import cn.edu.zju.bean.Drug;
import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.testsupport.DatabaseTestSupport;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

public class DetailDaoTest {

    private static final DrugDao drugDao = new DrugDao();
    private static final DrugLabelDao drugLabelDao = new DrugLabelDao();
    private static final DosingGuidelineDao dosingGuidelineDao = new DosingGuidelineDao();

    @BeforeClass
    public static void setUpClass() {
        DatabaseTestSupport.assertDatabaseReady();
        DatabaseTestSupport.assertTableHasData("drug");
        DatabaseTestSupport.assertTableHasData("drug_label");
        DatabaseTestSupport.assertTableHasData("dosing_guideline");
    }

    @Test
    public void findById_whenDrugExists_returnsMatchingDrug() {
        // Given
        String id = "PA10026";

        // When
        Drug drug = drugDao.findById(id);

        // Then
        assertNotNull("Expected drug record to be returned for a valid id.", drug);
        assertEquals("Expected returned drug id to match query id.", id, drug.getId());
    }

    @Test
    public void findById_whenDrugLabelExists_returnsMatchingDrugLabel() {
        // Given
        String id = "PA166104787";

        // When
        DrugLabel drugLabel = drugLabelDao.findById(id);

        // Then
        assertNotNull("Expected drug label record to be returned for a valid id.", drugLabel);
        assertEquals("Expected returned drug label id to match query id.", id, drugLabel.getId());
    }

    @Test
    public void findByDrugId_whenRelatedDrugLabelsExist_returnsNonEmptyList() {
        // Given
        String drugId = "PA10026";

        // When
        java.util.List<DrugLabel> relatedLabels = drugLabelDao.findByDrugId(drugId);

        // Then
        assertFalse("Expected at least one related drug label for the selected drug.", relatedLabels.isEmpty());
    }

    @Test
    public void findByDrugId_whenRelatedGuidelinesExist_returnsNonEmptyList() {
        // Given
        String drugId = "PA10026";

        // When
        java.util.List<DosingGuideline> relatedGuidelines = dosingGuidelineDao.findByDrugId(drugId);

        // Then
        assertFalse("Expected at least one related dosing guideline for the selected drug.", relatedGuidelines.isEmpty());
    }
}
