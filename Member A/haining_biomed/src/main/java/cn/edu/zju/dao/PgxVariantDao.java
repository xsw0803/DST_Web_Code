package cn.edu.zju.dao;

import cn.edu.zju.bean.PgxVariant;
import cn.edu.zju.dbutils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PgxVariantDao extends BaseDao {

    private static final Logger log = LoggerFactory.getLogger(PgxVariantDao.class);

    public List<PgxVariant> findByKeyword(String keyword) {
        String normalized = keyword == null ? "" : keyword.trim();
        boolean hasKeyword = !normalized.isEmpty();

        String sql = "select id, gene, variant_name, rs_id, function_status, phenotype, drug_id, evidence_source, interpretation from pgx_variant";
        if (hasKeyword) {
            sql += " where gene like ? or rs_id like ? or variant_name like ? or drug_id like ? or evidence_source like ?";
        }
        sql += " order by gene asc, rs_id asc";

        List<PgxVariant> list = new ArrayList<>();
        String querySql = sql;
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement ps = connection.prepareStatement(querySql);
                if (hasKeyword) {
                    String like = "%" + normalized + "%";
                    ps.setString(1, like);
                    ps.setString(2, like);
                    ps.setString(3, like);
                    ps.setString(4, like);
                    ps.setString(5, like);
                }
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    list.add(map(rs));
                }
            } catch (SQLException e) {
                log.error("Failed to query pgx_variant", e);
            }
        });
        return list;
    }

    public boolean save(PgxVariant variant) {
        final boolean[] success = {true};
        String sql = "insert into pgx_variant(gene, variant_name, rs_id, function_status, phenotype, drug_id, evidence_source, interpretation) values (?,?,?,?,?,?,?,?)";
        DBUtils.execSQL(connection -> {
            try {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, variant.getGene());
                ps.setString(2, variant.getVariantName());
                ps.setString(3, variant.getRsId());
                ps.setString(4, variant.getFunctionStatus());
                ps.setString(5, variant.getPhenotype());
                ps.setString(6, variant.getDrugId());
                ps.setString(7, variant.getEvidenceSource());
                ps.setString(8, variant.getInterpretation());
                ps.executeUpdate();
            } catch (SQLException e) {
                success[0] = false;
                log.error("Failed to save pgx_variant", e);
            }
        });
        return success[0];
    }

    private PgxVariant map(ResultSet rs) throws SQLException {
        PgxVariant item = new PgxVariant();
        item.setId(rs.getInt("id"));
        item.setGene(rs.getString("gene"));
        item.setVariantName(rs.getString("variant_name"));
        item.setRsId(rs.getString("rs_id"));
        item.setFunctionStatus(rs.getString("function_status"));
        item.setPhenotype(rs.getString("phenotype"));
        item.setDrugId(rs.getString("drug_id"));
        item.setEvidenceSource(rs.getString("evidence_source"));
        item.setInterpretation(rs.getString("interpretation"));
        return item;
    }
}
