package cn.edu.zju.dao;

import cn.edu.zju.bean.DosingGuideline;
import cn.edu.zju.bean.Drug;
import cn.edu.zju.dbutils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DosingGuidelineDao extends BaseDao {

    private static final Logger log = LoggerFactory.getLogger(DosingGuidelineDao.class);

    public boolean existsById(String id) {
        return super.existsById(id, "dosing_guideline");
    }

    public void saveDosingGuideline(DosingGuideline dosingGuideline) {
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("insert into dosing_guideline (id,obj_cls,name,recommendation,drug_id,source,summary_markdown,text_markdown,raw) values (?,?,?,?,?,?,?,?,?)");
                preparedStatement.setString(1, dosingGuideline.getId());
                preparedStatement.setString(2, dosingGuideline.getObjCls());
                preparedStatement.setString(3, dosingGuideline.getName());
                preparedStatement.setBoolean(4, dosingGuideline.isRecommendation());
                preparedStatement.setString(5, dosingGuideline.getDrugId());
                preparedStatement.setString(6, dosingGuideline.getSource());
                preparedStatement.setString(7, dosingGuideline.getSummaryMarkdown());
                preparedStatement.setString(8, dosingGuideline.getTextMarkdown());
                preparedStatement.setString(9, dosingGuideline.getRaw());
                preparedStatement.execute();
            } catch (SQLException e) {
                log.info("", e);
            }
        });

    }
    public List<DosingGuideline> findAll() {
        List<DosingGuideline> dosingGuidelines = new ArrayList<>();
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select id,obj_cls,name,recommendation,drug_id,source,summary_markdown,text_markdown,raw from dosing_guideline");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String objCls = resultSet.getString("obj_cls");
                    String name = resultSet.getString("name");
                    boolean recommendation = resultSet.getBoolean("recommendation");
                    String drugId = resultSet.getString("drug_id");
                    String source = resultSet.getString("source");
                    String summaryMarkdown = resultSet.getString("summary_markdown");
                    String textMarkdown = resultSet.getString("text_markdown");
                    String raw = resultSet.getString("raw");
                    DosingGuideline dosingGuideline = new DosingGuideline(id, objCls, name, recommendation, drugId, source, summaryMarkdown, textMarkdown, raw);
                    dosingGuidelines.add(dosingGuideline);
                }
            } catch (SQLException e) {
                log.info("", e);
            }
        });
        return dosingGuidelines;
    }

    public List<DosingGuideline> search(String keyword) {
        List<DosingGuideline> dosingGuidelines = new ArrayList<>();
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "select id, obj_cls, name, recommendation, drug_id, source, summary_markdown, text_markdown, raw " +
                                "from dosing_guideline " +
                                "where name like ? or drug_id like ? or source like ? or summary_markdown like ? " +
                                "order by name"
                );
                String pattern = "%" + keyword + "%";
                preparedStatement.setString(1, pattern);
                preparedStatement.setString(2, pattern);
                preparedStatement.setString(3, pattern);
                preparedStatement.setString(4, pattern);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String objCls = resultSet.getString("obj_cls");
                    String name = resultSet.getString("name");
                    boolean recommendation = resultSet.getBoolean("recommendation");
                    String drugId = resultSet.getString("drug_id");
                    String source = resultSet.getString("source");
                    String summaryMarkdown = resultSet.getString("summary_markdown");
                    String textMarkdown = resultSet.getString("text_markdown");
                    String raw = resultSet.getString("raw");
                    DosingGuideline dosingGuideline = new DosingGuideline(id, objCls, name, recommendation, drugId, source, summaryMarkdown, textMarkdown, raw);
                    dosingGuidelines.add(dosingGuideline);
                }
            } catch (SQLException e) {
                log.info("", e);
            }
        });
        return dosingGuidelines;
    }

    public DosingGuideline findById(String id) {
        List<DosingGuideline> dosingGuidelines = new ArrayList<>();
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "select id,obj_cls,name,recommendation,drug_id,source,summary_markdown,text_markdown,raw " +
                                "from dosing_guideline where id = ?"
                );
                preparedStatement.setString(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String guidelineId = resultSet.getString("id");
                    String objCls = resultSet.getString("obj_cls");
                    String name = resultSet.getString("name");
                    boolean recommendation = resultSet.getBoolean("recommendation");
                    String drugId = resultSet.getString("drug_id");
                    String source = resultSet.getString("source");
                    String summaryMarkdown = resultSet.getString("summary_markdown");
                    String textMarkdown = resultSet.getString("text_markdown");
                    String raw = resultSet.getString("raw");
                    dosingGuidelines.add(new DosingGuideline(guidelineId, objCls, name, recommendation, drugId, source,
                            summaryMarkdown, textMarkdown, raw));
                }
            } catch (SQLException e) {
                log.info("", e);
            }
        });
        return dosingGuidelines.isEmpty() ? null : dosingGuidelines.get(0);
    }

    public List<DosingGuideline> findByDrugId(String drugId) {
        List<DosingGuideline> dosingGuidelines = new ArrayList<>();
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "select id,obj_cls,name,recommendation,drug_id,source,summary_markdown,text_markdown,raw " +
                                "from dosing_guideline where drug_id = ? order by name"
                );
                preparedStatement.setString(1, drugId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String guidelineId = resultSet.getString("id");
                    String objCls = resultSet.getString("obj_cls");
                    String name = resultSet.getString("name");
                    boolean recommendation = resultSet.getBoolean("recommendation");
                    String relatedDrugId = resultSet.getString("drug_id");
                    String source = resultSet.getString("source");
                    String summaryMarkdown = resultSet.getString("summary_markdown");
                    String textMarkdown = resultSet.getString("text_markdown");
                    String raw = resultSet.getString("raw");
                    dosingGuidelines.add(new DosingGuideline(guidelineId, objCls, name, recommendation, relatedDrugId, source,
                            summaryMarkdown, textMarkdown, raw));
                }
            } catch (SQLException e) {
                log.info("", e);
            }
        });
        return dosingGuidelines;
    }
}
