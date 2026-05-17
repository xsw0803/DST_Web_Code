package cn.edu.zju.servlet;

import cn.edu.zju.bean.Drug;
import cn.edu.zju.dao.DrugDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DrugServlet",  urlPatterns = "/drugs")
public class DrugServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String q = request.getParameter("q");
        String biomarker = request.getParameter("biomarker");
        String normalizedBiomarker = normalizeBiomarker(biomarker);
        String normalizedQuery = q == null ? "" : q.trim();

        DrugDao drugDao = new DrugDao();
        List<Drug> all = drugDao.findByKeywordAndBiomarker(normalizedQuery, normalizedBiomarker);

        boolean hasSearch = !normalizedQuery.isEmpty() || !"all".equals(normalizedBiomarker);

        request.setAttribute("q", normalizedQuery);
        request.setAttribute("biomarker", normalizedBiomarker);
        request.setAttribute("hasSearch", hasSearch);
        request.setAttribute("resultCount", all.size());
        request.setAttribute("drugs", all);
        request.getRequestDispatcher("/views/drugs.jsp").forward(request, response);
    }

    private String normalizeBiomarker(String biomarker) {
        if (biomarker == null) {
            return "all";
        }
        String value = biomarker.trim().toLowerCase();
        if ("yes".equals(value) || "no".equals(value)) {
            return value;
        }
        return "all";
    }
}
