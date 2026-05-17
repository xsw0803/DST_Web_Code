package cn.edu.zju.dao;

import cn.edu.zju.bean.Drug;
import cn.edu.zju.dbutils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DrugDao extends BaseDao {

    private static final Logger log = LoggerFactory.getLogger(DrugDao.class);

    public boolean existsById(String id) {
        return super.existsById(id, "drug");
    }

    public void saveDrug(Drug drug) {
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("insert into drug (id, name, obj_cls, biomarker, drug_url) values    (?,?,?,?,?)");
                preparedStatement.setString(1, drug.getId());
                preparedStatement.setString(2, drug.getName());
                preparedStatement.setString(3, drug.getObjCls());
                preparedStatement.setBoolean(4, drug.isBiomarker());
                preparedStatement.setString(5, drug.getDrugUrl());
                preparedStatement.execute();
            } catch (SQLException e) {
                log.info("", e);
            }
        });

    }

    public List<Drug> findAll() {
        return findByKeywordAndBiomarker("", "all");
    }

    public List<Drug> findByKeyword(String keyword) {
        return findByKeywordAndBiomarker(keyword, "all");
    }

    public List<Drug> findByKeywordAndBiomarker(String keyword, String biomarkerStatus) {
        List<Drug> drugs = new ArrayList<>();
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        String normalizedBiomarker = biomarkerStatus == null ? "all" : biomarkerStatus.trim().toLowerCase();
        if (!"yes".equals(normalizedBiomarker) && !"no".equals(normalizedBiomarker)) {
            normalizedBiomarker = "all";
        }

        boolean hasKeyword = !normalizedKeyword.isEmpty();
        boolean filterBiomarkerYes = "yes".equals(normalizedBiomarker);
        boolean filterBiomarkerNo = "no".equals(normalizedBiomarker);

        String sql = "select id,name,obj_cls,drug_url,biomarker from drug";
        List<String> whereClauses = new ArrayList<>();
        if (hasKeyword) {
            whereClauses.add("(id like ? or name like ? or drug_url like ?)");
        }
        if (filterBiomarkerYes) {
            whereClauses.add("biomarker = 1");
        } else if (filterBiomarkerNo) {
            whereClauses.add("(biomarker = 0 or biomarker is null)");
        }
        if (!whereClauses.isEmpty()) {
            sql += " where " + String.join(" and ", whereClauses);
        }
        sql += " order by name asc";
        final String querySql = sql;

        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(querySql);
                int idx = 1;
                if (hasKeyword) {
                    String like = "%" + normalizedKeyword + "%";
                    preparedStatement.setString(idx++, like);
                    preparedStatement.setString(idx++, like);
                    preparedStatement.setString(idx, like);
                }
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    String objCls = resultSet.getString("obj_cls");
                    String drugUrl = resultSet.getString("drug_url");
                    boolean biomarker = resultSet.getBoolean("biomarker");
                    Drug drug = new Drug(id, name, biomarker, drugUrl, objCls);
                    drugs.add(drug);
                }
            } catch (SQLException e) {
                log.info("", e);
            }
        });
        return drugs;
    }

    public Drug findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        final Drug[] found = new Drug[1];
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "select id,name,obj_cls,drug_url,biomarker from drug where id = ?"
                );
                preparedStatement.setString(1, id.trim());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    found[0] = new Drug(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            resultSet.getBoolean("biomarker"),
                            resultSet.getString("drug_url"),
                            resultSet.getString("obj_cls")
                    );
                }
            } catch (SQLException e) {
                log.info("", e);
            }
        });
        return found[0];
    }

}
