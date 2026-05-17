package cn.edu.zju.dao;

import cn.edu.zju.bean.DosingGuideline;
import cn.edu.zju.testsupport.DatabaseTestSupport;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DosingGuidelineDaoTest {

    private static final DosingGuidelineDao dosingGuidelineDao = new DosingGuidelineDao();

    @BeforeClass
    public static void setUpClass() {
        DatabaseTestSupport.assertDatabaseReady();
        DatabaseTestSupport.assertTableHasData("dosing_guideline");
    }

    @Test
    public void search_whenKeywordMatchesSource_returnsMatchedGuidelines() {
        // Given
        String keyword = "CPIC";

        // When
        List<DosingGuideline> results = dosingGuidelineDao.search(keyword);

        // Then
        assertFalse("Expected search results for keyword " + keyword, results.isEmpty());
        assertTrue("Each result should match the keyword in name, drug id, source, or summary.",
                results.stream().allMatch(guideline -> matchesKeyword(guideline, keyword)));
    }

    @Test
    public void search_whenKeywordMatchesDrugOrSummary_returnsMatchedGuidelines() {
        // Given
        String keyword = "fluoxetine";

        // When
        List<DosingGuideline> results = dosingGuidelineDao.search(keyword);

        // Then
        assertFalse("Expected search results for keyword " + keyword, results.isEmpty());
        assertTrue("Each result should match the keyword in name, drug id, source, or summary.",
                results.stream().allMatch(guideline -> matchesKeyword(guideline, keyword)));
    }

    @Test
    public void search_whenKeywordDoesNotExist_returnsEmptyList() {
        // Given
        String keyword = "zzzz_non_existing_guideline_keyword";

        // When
        List<DosingGuideline> results = dosingGuidelineDao.search(keyword);

        // Then
        assertTrue("Expected no results for a non-existing keyword.", results.isEmpty());
    }

    private boolean matchesKeyword(DosingGuideline guideline, String keyword) {
        String normalizedKeyword = keyword.toLowerCase();
        return containsIgnoreCase(guideline.getName(), normalizedKeyword)
                || containsIgnoreCase(guideline.getDrugId(), normalizedKeyword)
                || containsIgnoreCase(guideline.getSource(), normalizedKeyword)
                || containsIgnoreCase(guideline.getSummaryMarkdown(), normalizedKeyword);
    }

    private boolean containsIgnoreCase(String value, String normalizedKeyword) {
        return value != null && value.toLowerCase().contains(normalizedKeyword);
    }
}
