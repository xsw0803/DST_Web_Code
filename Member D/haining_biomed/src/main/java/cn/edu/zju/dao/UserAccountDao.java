package cn.edu.zju.dao;

import cn.edu.zju.bean.UserAccount;
import cn.edu.zju.dbutils.DBUtils;

import java.sql.*;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class UserAccountDao extends BaseDao {

    public boolean existsByUsername(String username) {
        AtomicReference<Boolean> exists = new AtomicReference<>(false);
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select 1 from user_account where username = ?");
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                exists.set(resultSet.next());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return exists.get();
    }

    public int save(String username, String password) {
        AtomicReference<Integer> key = new AtomicReference<>(0);
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "insert into user_account(username, password, created_at) values (?,?,?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setTimestamp(3, new Timestamp(new Date().getTime()));
                preparedStatement.executeUpdate();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    key.set(generatedKeys.getInt(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                key.set(0);
            }
        });
        return key.get();
    }

    public UserAccount findByUsername(String username) {
        AtomicReference<UserAccount> user = new AtomicReference<>();
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "select id, username, password, created_at from user_account where username = ?"
                );
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    user.set(new UserAccount(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getTimestamp("created_at") == null ? null : new Date(resultSet.getTimestamp("created_at").getTime())
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return user.get();
    }
}
