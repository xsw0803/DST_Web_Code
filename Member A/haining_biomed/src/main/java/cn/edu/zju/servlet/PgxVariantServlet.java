package cn.edu.zju.servlet;

import cn.edu.zju.bean.PgxVariant;
import cn.edu.zju.dao.PgxVariantDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "PgxVariantServlet", urlPatterns = "/pgxVariants")
public class PgxVariantServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = trimToEmpty(request.getParameter("keyword"));
        PgxVariantDao dao = new PgxVariantDao();
        List<PgxVariant> variants = dao.findByKeyword(keyword);
        request.setAttribute("keyword", keyword);
        request.setAttribute("variants", variants);
        request.getRequestDispatcher("/views/pgx_variants.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PgxVariant variant = new PgxVariant();
        variant.setGene(trimToEmpty(request.getParameter("gene")));
        variant.setVariantName(trimToEmpty(request.getParameter("variantName")));
        variant.setRsId(trimToEmpty(request.getParameter("rsId")));
        variant.setFunctionStatus(trimToEmpty(request.getParameter("functionStatus")));
        variant.setPhenotype(trimToEmpty(request.getParameter("phenotype")));
        variant.setDrugId(trimToEmpty(request.getParameter("drugId")));
        variant.setEvidenceSource(trimToEmpty(request.getParameter("evidenceSource")));
        variant.setInterpretation(trimToEmpty(request.getParameter("interpretation")));

        if (variant.getGene().isEmpty() || variant.getRsId().isEmpty()) {
            request.setAttribute("error", "Gene and rsID are required to add a PGx variant record.");
            doGet(request, response);
            return;
        }

        PgxVariantDao dao = new PgxVariantDao();
        boolean saved = dao.save(variant);
        if (!saved) {
            request.setAttribute("error", "Failed to save PGx variant record. Please verify rsID/drug combination uniqueness.");
            doGet(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/pgxVariants");
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
