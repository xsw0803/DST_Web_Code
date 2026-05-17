package cn.edu.zju.dao;

import cn.edu.zju.bean.Drug;
import cn.edu.zju.testsupport.DatabaseTestSupport;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DrugDaoTest {

    private static final DrugDao drugDao = new DrugDao();

    @BeforeClass
    public static void setUpClass() {
        DatabaseTestSupport.assertDatabaseReady();
        DatabaseTestSupport.assertTableHasData("drug");
    }

    @Test
    public void searchByName_whenKeywordExists_returnsMatchedDrugs() {
        // Given
        String keyword = "warfarin";

        // When
        List<Drug> results = drugDao.searchByName(keyword);

        // Then
        assertFalse("Expected search results for keyword " + keyword, results.isEmpty());
        assertTrue("All returned drug names should contain the keyword.",
                results.stream().allMatch(drug ->
                        drug.getName() != null && drug.getName().toLowerCase().contains(keyword)));
    }

    @Test
    public void searchByName_whenKeywordDoesNotExist_returnsEmptyList() {
        // Given
        String keyword = "zzzz_non_existing_drug_keyword";

        // When
        List<Drug> results = drugDao.searchByName(keyword);

        // Then
        assertTrue("Expected no results for a non-existing keyword.", results.isEmpty());
    }
}
