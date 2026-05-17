package cn.edu.zju.dao;

import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.dbutils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DrugLabelDao extends BaseDao {

    private static final Logger log = LoggerFactory.getLogger(DrugLabelDao.class);

    public boolean existsById(String id) {
        return super.existsById(id, "drug_label");
    }

    public void saveDrugLabel(DrugLabel drugLabel) {
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("insert into drug_label (id,name,obj_cls,alternate_drug_available,dosing_information,prescribing_markdown,source,text_markdown,summary_markdown,raw,drug_id) values (?,?,?,?,?,?,?,?,?,?,?)");
                preparedStatement.setString(1, drugLabel.getId());
                preparedStatement.setString(2, drugLabel.getName());
                preparedStatement.setString(3, drugLabel.getObjCls());
                preparedStatement.setBoolean(4, drugLabel.isAlternateDrugAvailable());
                preparedStatement.setBoolean(5, drugLabel.isDosingInformation());
                preparedStatement.setString(6, drugLabel.getPrescribingMarkdown());
                preparedStatement.setString(7, drugLabel.getSource());
                preparedStatement.setString(8, drugLabel.getTextMarkdown());
                preparedStatement.setString(9, drugLabel.getSummaryMarkdown());
                preparedStatement.setString(10, drugLabel.getRaw());
                preparedStatement.setString(11, drugLabel.getDrugId());
                preparedStatement.execute();
            } catch (SQLException e) {
                log.info("", e);
            }
        });

    }

    public List<DrugLabel> findAll() {
        return findByKeywordAndDosingStatus("", "all");
    }

    public List<DrugLabel> findByKeyword(String keyword) {
        return findByKeywordAndDosingStatus(keyword, "all");
    }

    public List<DrugLabel> findByKeywordAndDosingStatus(String keyword, String dosingStatus) {
        List<DrugLabel> drugLabels = new ArrayList<>();
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        String normalizedDosingStatus = dosingStatus == null ? "all" : dosingStatus.trim().toLowerCase();
        if (!"yes".equals(normalizedDosingStatus) && !"no".equals(normalizedDosingStatus)) {
            normalizedDosingStatus = "all";
        }

        boolean hasKeyword = !normalizedKeyword.isEmpty();
        boolean filterYes = "yes".equals(normalizedDosingStatus);
        boolean filterNo = "no".equals(normalizedDosingStatus);

        String sql = "select dl.id, dl.name, dl.obj_cls, dl.alternate_drug_available, dl.dosing_information, dl.prescribing_markdown, dl.source, dl.text_markdown, dl.summary_markdown, dl.raw, dl.drug_id, d.name as drug_name " +
                "from drug_label dl left join drug d on dl.drug_id = d.id";
        List<String> whereClauses = new ArrayList<>();
        if (hasKeyword) {
            whereClauses.add("(dl.id like ? or dl.source like ? or dl.summary_markdown like ? or dl.drug_id like ? or d.name like ?)");
        }
        if (filterYes) {
            whereClauses.add("dosing_information = 1");
        } else if (filterNo) {
            whereClauses.add("(dosing_information = 0 or dosing_information is null)");
        }
        if (!whereClauses.isEmpty()) {
            sql += " where " + String.join(" and ", whereClauses);
        }
        sql += " order by id asc";
        final String querySql = sql;

        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(querySql);
                int idx = 1;
                if (hasKeyword) {
                    String like = "%" + normalizedKeyword + "%";
                    preparedStatement.setString(idx++, like);
                    preparedStatement.setString(idx++, like);
                    preparedStatement.setString(idx++, like);
                    preparedStatement.setString(idx++, like);
                    preparedStatement.setString(idx, like);
                }
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    String obj_cls = resultSet.getString("obj_cls");
                    boolean alternate_drug_available = resultSet.getBoolean("alternate_drug_available");
                    boolean dosing_information = resultSet.getBoolean("dosing_information");
                    String prescribing_markdown = resultSet.getString("prescribing_markdown");
                    String source = resultSet.getString("source");
                    String text_markdown = resultSet.getString("text_markdown");
                    String summary_markdown = resultSet.getString("summary_markdown");
                    String raw = resultSet.getString("raw");
                    String drug_id = resultSet.getString("drug_id");
                    String drug_name = resultSet.getString("drug_name");
                    DrugLabel drugLabel = new DrugLabel(id, name, obj_cls, alternate_drug_available, dosing_information, prescribing_markdown, source, text_markdown, summary_markdown, raw, drug_id);
                    drugLabel.setDrugName(drug_name);
                    drugLabels.add(drugLabel);
                }
            } catch (SQLException e) {
                log.info("", e);
            }
        });
        return drugLabels;
    }

    public List<DrugLabel> findByDrugId(String drugId) {
        List<DrugLabel> drugLabels = new ArrayList<>();
        if (drugId == null || drugId.trim().isEmpty()) {
            return drugLabels;
        }

        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "select dl.id, dl.name, dl.obj_cls, dl.alternate_drug_available, dl.dosing_information, dl.prescribing_markdown, dl.source, dl.text_markdown, dl.summary_markdown, dl.raw, dl.drug_id, d.name as drug_name " +
                                "from drug_label dl left join drug d on dl.drug_id = d.id where dl.drug_id = ?"
                );
                preparedStatement.setString(1, drugId.trim());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    String obj_cls = resultSet.getString("obj_cls");
                    boolean alternate_drug_available = resultSet.getBoolean("alternate_drug_available");
                    boolean dosing_information = resultSet.getBoolean("dosing_information");
                    String prescribing_markdown = resultSet.getString("prescribing_markdown");
                    String source = resultSet.getString("source");
                    String text_markdown = resultSet.getString("text_markdown");
                    String summary_markdown = resultSet.getString("summary_markdown");
                    String raw = resultSet.getString("raw");
                    String currentDrugId = resultSet.getString("drug_id");
                    String drugName = resultSet.getString("drug_name");
                    DrugLabel drugLabel = new DrugLabel(id, name, obj_cls, alternate_drug_available, dosing_information, prescribing_markdown, source, text_markdown, summary_markdown, raw, currentDrugId);
                    drugLabel.setDrugName(drugName);
                    drugLabels.add(drugLabel);
                }
            } catch (SQLException e) {
                log.info("", e);
            }
        });
        return drugLabels;
    }
}
