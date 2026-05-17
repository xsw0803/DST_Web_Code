package cn.edu.zju.dao;

import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.testsupport.DatabaseTestSupport;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DrugLabelDaoTest {

    private static final DrugLabelDao drugLabelDao = new DrugLabelDao();

    @BeforeClass
    public static void setUpClass() {
        DatabaseTestSupport.assertDatabaseReady();
        DatabaseTestSupport.assertTableHasData("drug_label");
    }

    @Test
    public void search_whenKeywordMatchesSource_returnsMatchedDrugLabels() {
        // Given
        String keyword = "FDA";

        // When
        List<DrugLabel> results = drugLabelDao.search(keyword);

        // Then
        assertFalse("Expected search results for keyword " + keyword, results.isEmpty());
        assertTrue("Each result should match the keyword in name, source, or summary.",
                results.stream().allMatch(label -> matchesKeyword(label, keyword)));
    }

    @Test
    public void search_whenKeywordMatchesSummary_returnsMatchedDrugLabels() {
        // Given
        String keyword = "CYP2D6";

        // When
        List<DrugLabel> results = drugLabelDao.search(keyword);

        // Then
        assertFalse("Expected search results for keyword " + keyword, results.isEmpty());
        assertTrue("Each result should match the keyword in name, source, or summary.",
                results.stream().allMatch(label -> matchesKeyword(label, keyword)));
    }

    @Test
    public void search_whenKeywordDoesNotExist_returnsEmptyList() {
        // Given
        String keyword = "zzzz_non_existing_label_keyword";

        // When
        List<DrugLabel> results = drugLabelDao.search(keyword);

        // Then
        assertTrue("Expected no results for a non-existing keyword.", results.isEmpty());
    }

    private boolean matchesKeyword(DrugLabel label, String keyword) {
        String normalizedKeyword = keyword.toLowerCase();
        return containsIgnoreCase(label.getName(), normalizedKeyword)
                || containsIgnoreCase(label.getSource(), normalizedKeyword)
                || containsIgnoreCase(label.getSummaryMarkdown(), normalizedKeyword);
    }

    private boolean containsIgnoreCase(String value, String normalizedKeyword) {
        return value != null && value.toLowerCase().contains(normalizedKeyword);
    }
}
