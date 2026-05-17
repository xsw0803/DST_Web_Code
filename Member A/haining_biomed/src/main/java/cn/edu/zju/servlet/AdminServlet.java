package cn.edu.zju.servlet;

import cn.edu.zju.dbutils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "AdminServlet", urlPatterns = "/admin")
public class AdminServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AdminServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("drugCount", queryCount("select count(*) from drug"));
        request.setAttribute("drugLabelCount", queryCount("select count(*) from drug_label"));
        request.setAttribute("guidelineCount", queryCount("select count(*) from dosing_guideline"));
        request.setAttribute("pgxVariantCount", queryCount("select count(*) from pgx_variant"));
        request.setAttribute("uploadedSampleCount", queryCount("select count(distinct sample_id) from annovar"));
        request.setAttribute("unresolvedGuidelineDrugCount",
                queryCount("select count(*) from dosing_guideline dg left join drug d on dg.drug_id = d.id where dg.drug_id is not null and dg.drug_id <> '' and d.id is null"));
        request.setAttribute("unresolvedPgxDrugCount",
                queryCount("select count(*) from pgx_variant pv left join drug d on pv.drug_id = d.id where pv.drug_id is not null and pv.drug_id <> '' and d.id is null"));
        request.getRequestDispatcher("/views/admin.jsp").forward(request, response);
    }

    private int queryCount(String sql) {
        final int[] count = {0};
        DBUtils.execSQL(connection -> {
            if (connection == null) {
                return;
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    count[0] = resultSet.getInt(1);
                }
            } catch (SQLException e) {
                log.error("Failed to execute count query: {}", sql, e);
            }
        });
        return count[0];
    }
}
