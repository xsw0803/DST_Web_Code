package cn.edu.zju.dao;

import cn.edu.zju.bean.SavedItem;
import cn.edu.zju.dbutils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SavedItemDao {

    private static final Logger log = LoggerFactory.getLogger(SavedItemDao.class);

    public boolean exists(String username, String itemType, String itemId) {
        final boolean[] found = {false};
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "select 1 from saved_item where username = ? and item_type = ? and item_id = ? limit 1"
                );
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, itemType);
                preparedStatement.setString(3, itemId);
                ResultSet resultSet = preparedStatement.executeQuery();
                found[0] = resultSet.next();
            } catch (SQLException e) {
                log.info("", e);
            }
        });
        return found[0];
    }

    public void save(String username, String itemType, String itemId) {
        if (exists(username, itemType, itemId)) {
            return;
        }
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "insert into saved_item (username, item_type, item_id, created_at) values (?,?,?,now())"
                );
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, itemType);
                preparedStatement.setString(3, itemId);
                preparedStatement.execute();
            } catch (SQLException e) {
                log.info("", e);
            }
        });
    }

    public void delete(String username, String itemType, String itemId) {
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "delete from saved_item where username = ? and item_type = ? and item_id = ?"
                );
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, itemType);
                preparedStatement.setString(3, itemId);
                preparedStatement.execute();
            } catch (SQLException e) {
                log.info("", e);
            }
        });
    }

    public List<SavedItem> findByUsername(String username) {
        List<SavedItem> savedItems = new ArrayList<>();
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "select id, username, item_type, item_id, created_at from saved_item where username = ? order by created_at desc, id desc"
                );
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    String savedUsername = resultSet.getString("username");
                    String itemType = resultSet.getString("item_type");
                    String itemId = resultSet.getString("item_id");
                    Date createdAt = resultSet.getTimestamp("created_at");
                    savedItems.add(new SavedItem(id, savedUsername, itemType, itemId, createdAt));
                }
            } catch (SQLException e) {
                log.info("", e);
            }
        });
        return savedItems;
    }

    public Set<String> findItemIdsByUsernameAndType(String username, String itemType) {
        Set<String> itemIds = new HashSet<>();
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "select item_id from saved_item where username = ? and item_type = ?"
                );
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, itemType);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    itemIds.add(resultSet.getString("item_id"));
                }
            } catch (SQLException e) {
                log.info("", e);
            }
        });
        return itemIds;
    }
}
