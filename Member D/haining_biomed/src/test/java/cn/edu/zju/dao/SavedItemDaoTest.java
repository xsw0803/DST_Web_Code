package cn.edu.zju.dao;

import cn.edu.zju.testsupport.DatabaseTestSupport;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SavedItemDaoTest {

    private static final SavedItemDao savedItemDao = new SavedItemDao();

    private static final String USERNAME = "junit_saved_item_user";
    private static final String ITEM_TYPE = "TEST_LABEL";
    private static final String ITEM_ID = "PA166104787";

    @BeforeClass
    public static void setUpClass() {
        DatabaseTestSupport.assertDatabaseReady();
    }

    @After
    public void tearDown() {
        savedItemDao.delete(USERNAME, ITEM_TYPE, ITEM_ID);
    }

    @Test
    public void save_whenItemDoesNotExist_persistsSavedItem() {
        // Given
        savedItemDao.delete(USERNAME, ITEM_TYPE, ITEM_ID);

        // When
        savedItemDao.save(USERNAME, ITEM_TYPE, ITEM_ID);

        // Then
        assertTrue("Expected saved item to exist after save operation.",
                savedItemDao.exists(USERNAME, ITEM_TYPE, ITEM_ID));
    }

    @Test
    public void delete_whenSavedItemExists_removesSavedItem() {
        // Given
        savedItemDao.save(USERNAME, ITEM_TYPE, ITEM_ID);

        // When
        savedItemDao.delete(USERNAME, ITEM_TYPE, ITEM_ID);

        // Then
        assertFalse("Expected saved item to be removed after delete operation.",
                savedItemDao.exists(USERNAME, ITEM_TYPE, ITEM_ID));
    }

    @Test
    public void findItemIdsByUsernameAndType_whenSavedItemExists_returnsItemId() {
        // Given
        savedItemDao.save(USERNAME, ITEM_TYPE, ITEM_ID);

        // When
        Set<String> itemIds = savedItemDao.findItemIdsByUsernameAndType(USERNAME, ITEM_TYPE);

        // Then
        assertTrue("Expected saved item id to be returned for the user and type.",
                itemIds.contains(ITEM_ID));
    }
}
