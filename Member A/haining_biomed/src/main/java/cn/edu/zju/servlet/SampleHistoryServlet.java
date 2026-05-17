package cn.edu.zju.servlet;

import cn.edu.zju.bean.MutationMatchResult;
import cn.edu.zju.bean.Sample;
import cn.edu.zju.bean.SampleHistoryRow;
import cn.edu.zju.bean.AnnovarVariant;
import cn.edu.zju.dao.AnnovarDao;
import cn.edu.zju.dao.MutationMatchingDao;
import cn.edu.zju.dao.SampleDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@WebServlet(name = "SampleHistoryServlet", urlPatterns = "/sampleHistory")
public class SampleHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SampleDao sampleDao = new SampleDao();
        AnnovarDao annovarDao = new AnnovarDao();
        MutationMatchingDao mutationMatchingDao = new MutationMatchingDao();

        List<Sample> samples = sampleDao.findAll();
        List<SampleHistoryRow> rows = new ArrayList<>();
        for (Sample sample : samples) {
            int sampleId = sample.getId();
            int variantCount = annovarDao.countBySampleId(sampleId);
            List<AnnovarVariant> functionalVariants = new ArrayList<>();
            for (AnnovarVariant variant : annovarDao.findBySampleId(sampleId)) {
                if (AnnovarDao.isPotentiallyFunctional(variant)) {
                    functionalVariants.add(variant);
                }
            }
            List<MutationMatchResult> matches = mutationMatchingDao.findMatchesWithFallback(functionalVariants);
            Set<String> matchedGenes = new LinkedHashSet<>();
            Set<String> matchedDrugIds = new LinkedHashSet<>();
            for (MutationMatchResult match : matches) {
                if (match.getGene() != null && !match.getGene().trim().isEmpty()) {
                    matchedGenes.add(match.getGene().trim());
                }
                if (match.getDrugId() != null && !match.getDrugId().trim().isEmpty()) {
                    matchedDrugIds.add(match.getDrugId());
                }
            }

            SampleHistoryRow row = new SampleHistoryRow();
            row.setSampleId(sampleId);
            row.setFileName(sample.getFileName() == null || sample.getFileName().trim().isEmpty() ? "Not recorded" : sample.getFileName());
            row.setUploadTime(sample.getCreatedAt());
            row.setUploadedBy(sample.getUploadedBy());
            row.setVariantCount(variantCount);
            row.setMatchedGeneCount(matchedGenes.size());
            row.setMatchedDrugCount(matchedDrugIds.size());
            row.setStatus(variantCount > 0 ? "Completed" : "Pending");
            rows.add(row);
        }

        request.setAttribute("rows", rows);
        request.getRequestDispatcher("/views/sample_history.jsp").forward(request, response);
    }
}
