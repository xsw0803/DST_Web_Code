package cn.edu.zju.dao;

import cn.edu.zju.bean.Sample;
import cn.edu.zju.dbutils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SampleDao extends BaseDao {

    public int save(String uploadedBy) {
        return save(uploadedBy, null);
    }

    public int save(String uploadedBy, String fileName) {
        AtomicInteger key = new AtomicInteger();
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement;
                if (fileName != null && !fileName.trim().isEmpty()) {
                    preparedStatement = connection.prepareStatement("insert into sample(created_at, uploaded_by, file_name) values (?,?,?)", Statement.RETURN_GENERATED_KEYS);
                } else {
                    preparedStatement = connection.prepareStatement("insert into sample(created_at, uploaded_by) values (?,?)", Statement.RETURN_GENERATED_KEYS);
                }
                preparedStatement.setTimestamp(1, new Timestamp(new Date().getTime()));
                preparedStatement.setString(2, uploadedBy);
                if (fileName != null && !fileName.trim().isEmpty()) {
                    preparedStatement.setString(3, fileName);
                }
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    key.set(resultSet.getInt(1));
                }
            } catch (SQLException e) {
                // Backward compatibility: allow insert when file_name column is not yet present.
                if (fileName != null && !fileName.trim().isEmpty()) {
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement("insert into sample(created_at, uploaded_by) values (?,?)", Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.setTimestamp(1, new Timestamp(new Date().getTime()));
                        preparedStatement.setString(2, uploadedBy);
                        preparedStatement.executeUpdate();
                        ResultSet resultSet = preparedStatement.getGeneratedKeys();
                        if (resultSet.next()) {
                            key.set(resultSet.getInt(1));
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
        return key.get();
    }

    public List<Sample> findAll() {
        List<Sample> samples = new ArrayList<>();
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select id, created_at, uploaded_by, file_name from sample order by id desc");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int sampleId = resultSet.getInt("id");
                    Date createdAt = new Date(resultSet.getTimestamp("created_at").getTime());
                    String uploadedBy = resultSet.getString("uploaded_by");
                    String fileName = resultSet.getString("file_name");
                    Sample sample = new Sample(sampleId, createdAt, uploadedBy, fileName);
                    samples.add(sample);
                }
            } catch (SQLException e) {
                // Backward compatibility: allow running against schema without file_name.
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("select id, created_at, uploaded_by from sample order by id desc");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int sampleId = resultSet.getInt("id");
                        Date createdAt = new Date(resultSet.getTimestamp("created_at").getTime());
                        String uploadedBy = resultSet.getString("uploaded_by");
                        Sample sample = new Sample(sampleId, createdAt, uploadedBy, "Not recorded");
                        samples.add(sample);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        return samples;
    }

    public Sample findById(int id) {
        AtomicReference<Sample> sample = new AtomicReference<>();
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select id, created_at, uploaded_by, file_name from sample where id = ?");
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int sampleId = resultSet.getInt("id");
                    Date createdAt = new Date(resultSet.getTimestamp("created_at").getTime());
                    String uploadedBy = resultSet.getString("uploaded_by");
                    String fileName = resultSet.getString("file_name");
                    sample.set(new Sample(sampleId, createdAt, uploadedBy, fileName));
                }
            } catch (SQLException e) {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("select id, created_at, uploaded_by from sample where id = ?");
                    preparedStatement.setInt(1, id);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        int sampleId = resultSet.getInt("id");
                        Date createdAt = new Date(resultSet.getTimestamp("created_at").getTime());
                        String uploadedBy = resultSet.getString("uploaded_by");
                        sample.set(new Sample(sampleId, createdAt, uploadedBy, "Not recorded"));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        return sample.get();
    }
}
