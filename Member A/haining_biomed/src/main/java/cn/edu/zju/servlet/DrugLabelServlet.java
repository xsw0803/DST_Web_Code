package cn.edu.zju.servlet;

import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.dao.DrugLabelDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DrugLabelServlet",  urlPatterns = "/drugLabels")
public class DrugLabelServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String q = request.getParameter("q");
        String dosing = request.getParameter("dosing");
        String normalizedDosing = normalizeStatus(dosing);
        String normalizedQuery = q == null ? "" : q.trim();

        DrugLabelDao drugLabelDao = new DrugLabelDao();
        List<DrugLabel> all = drugLabelDao.findByKeywordAndDosingStatus(normalizedQuery, normalizedDosing);
        boolean hasSearch = !normalizedQuery.isEmpty() || !"all".equals(normalizedDosing);

        request.setAttribute("q", normalizedQuery);
        request.setAttribute("dosing", normalizedDosing);
        request.setAttribute("hasSearch", hasSearch);
        request.setAttribute("resultCount", all.size());
        request.setAttribute("drugLabels", all);
        request.getRequestDispatcher("/views/drug_labels.jsp").forward(request, response);
    }

    private String normalizeStatus(String status) {
        if (status == null) {
            return "all";
        }
        String value = status.trim().toLowerCase();
        if ("yes".equals(value) || "no".equals(value)) {
            return value;
        }
        return "all";
    }
}
