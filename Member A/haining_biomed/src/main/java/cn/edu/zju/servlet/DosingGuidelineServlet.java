package cn.edu.zju.servlet;

import cn.edu.zju.bean.DosingGuideline;
import cn.edu.zju.dao.DosingGuidelineDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DosingGuidelineServlet",  urlPatterns = "/dosingGuideline")
public class DosingGuidelineServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String q = request.getParameter("q");
        String recommendation = request.getParameter("recommendation");
        String normalizedRecommendation = normalizeStatus(recommendation);
        String normalizedQuery = q == null ? "" : q.trim();

        DosingGuidelineDao dosingGuidelineDao = new DosingGuidelineDao();
        List<DosingGuideline> dosingGuidelines = dosingGuidelineDao.findByKeywordAndRecommendationStatus(normalizedQuery, normalizedRecommendation);
        boolean hasSearch = !normalizedQuery.isEmpty() || !"all".equals(normalizedRecommendation);

        request.setAttribute("q", normalizedQuery);
        request.setAttribute("recommendation", normalizedRecommendation);
        request.setAttribute("hasSearch", hasSearch);
        request.setAttribute("resultCount", dosingGuidelines.size());
        request.setAttribute("dosingGuidelines", dosingGuidelines);
        request.getRequestDispatcher("/views/dosing_guideline.jsp").forward(request, response);
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
