package cn.edu.zju.servlet;

import cn.edu.zju.bean.AnnovarVariant;
import cn.edu.zju.dao.AnnovarDao;
import cn.edu.zju.filter.AuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "MutationSearchServlet", urlPatterns = "/mutationSearch")
public class MutationSearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currentRole = (String) request.getSession().getAttribute(AuthenticationFilter.SESSION_ROLE);
        boolean canUseAdvancedFilters = AuthenticationFilter.ROLE_PROFESSIONAL.equals(currentRole)
                || AuthenticationFilter.ROLE_ADMIN.equals(currentRole);

        String sampleIdParam = trimToEmpty(request.getParameter("sampleId"));
        String gene = trimToEmpty(request.getParameter("gene"));
        String rsId = trimToEmpty(request.getParameter("rsId"));
        String chr = trimToEmpty(request.getParameter("chr"));
        String position = trimToEmpty(request.getParameter("position"));

        boolean hasQuery = !(sampleIdParam.isEmpty() && gene.isEmpty() && rsId.isEmpty() && chr.isEmpty() && position.isEmpty());
        List<AnnovarVariant> variants = Collections.emptyList();
        if (hasQuery) {
            Integer sampleId = parseSampleId(sampleIdParam);
            AnnovarDao annovarDao = new AnnovarDao();
            variants = annovarDao.searchVariants(sampleId, gene, rsId, chr, position);
            for (AnnovarVariant variant : variants) {
                variant.setFunctionalStatus(resolveFunctionalStatus(variant));
            }
        }

        request.setAttribute("sampleId", sampleIdParam);
        request.setAttribute("gene", gene);
        request.setAttribute("rsId", rsId);
        request.setAttribute("chr", chr);
        request.setAttribute("position", position);
        request.setAttribute("hasQuery", hasQuery);
        request.setAttribute("variants", variants);
        request.setAttribute("resultCount", variants.size());
        request.setAttribute("canUseAdvancedFilters", canUseAdvancedFilters);
        request.getRequestDispatcher("/views/mutation_search.jsp").forward(request, response);
    }

    private String resolveFunctionalStatus(AnnovarVariant variant) {
        if (AnnovarDao.isPotentiallyFunctional(variant)) {
            return "Potentially relevant";
        }
        return "Likely non-functional";
    }

    private Integer parseSampleId(String sampleId) {
        if (sampleId == null || sampleId.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(sampleId.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
