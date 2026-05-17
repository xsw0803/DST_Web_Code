package cn.edu.zju.dao;

import cn.edu.zju.bean.User;
import cn.edu.zju.dbutils.DBUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class UserDao extends BaseDao {

    public User findByUsernameAndPassword(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        String sql = "select id, username, password, role, display_name, created_at from users where username = ? and password = ?";
        AtomicReference<User> userRef = new AtomicReference<>();
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, username.trim());
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRole(resultSet.getString("role"));
                    user.setDisplayName(resultSet.getString("display_name"));
                    Timestamp createdAt = resultSet.getTimestamp("created_at");
                    if (createdAt != null) {
                        user.setCreatedAt(new Date(createdAt.getTime()));
                    }
                    userRef.set(user);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return userRef.get();
    }

    public boolean existsByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        String sql = "select 1 from users where username = ?";
        AtomicBoolean exists = new AtomicBoolean(false);
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, username.trim());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    exists.set(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return exists.get();
    }

    public int saveUser(String username, String password, String role, String displayName) {
        if (username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()
                || role == null || role.trim().isEmpty()) {
            return 0;
        }

        String sql = "insert into users(username, password, role, display_name, created_at) values (?, ?, ?, ?, ?)";
        AtomicInteger generatedId = new AtomicInteger(0);
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, username.trim());
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, role.trim());
                preparedStatement.setString(4, (displayName == null || displayName.trim().isEmpty()) ? username.trim() : displayName.trim());
                preparedStatement.setTimestamp(5, new Timestamp(new Date().getTime()));
                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    ResultSet rs = preparedStatement.getGeneratedKeys();
                    if (rs.next()) {
                        generatedId.set(rs.getInt(1));
                    } else {
                        generatedId.set(1);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return generatedId.get();
    }
}
