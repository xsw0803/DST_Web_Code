package cn.edu.zju.testsupport;

import cn.edu.zju.dbutils.DBUtils;
import org.junit.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DatabaseTestSupport {

    private DatabaseTestSupport() {
    }

    public static void assertDatabaseReady() {
        try (Connection connection = DBUtils.getConnection()) {
            Assert.assertNotNull("Database connection should not be null. Check app.properties and local MySQL.", connection);
        } catch (SQLException e) {
            throw new AssertionError("Failed to verify database connection.", e);
        }
    }

    public static void assertTableHasData(String tableName) {
        String sql = "select count(*) from " + tableName;
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            Assert.assertNotNull("Database connection should not be null. Check app.properties and local MySQL.", connection);
            Assert.assertTrue("Expected at least one row in table " + tableName, resultSet.next());
            Assert.assertTrue("Expected seeded data in table " + tableName, resultSet.getInt(1) > 0);
        } catch (SQLException e) {
            throw new AssertionError("Failed to verify seeded data for table " + tableName, e);
        }
    }
}
