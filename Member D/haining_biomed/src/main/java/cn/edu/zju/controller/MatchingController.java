package cn.edu.zju.controller;

import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.bean.Sample;
import cn.edu.zju.dao.AnnovarDao;
import cn.edu.zju.dao.DrugLabelDao;
import cn.edu.zju.dao.SavedItemDao;
import cn.edu.zju.dao.SampleDao;
import cn.edu.zju.service.ExplanationService;
import cn.edu.zju.service.GeminiExplanationService;
import cn.edu.zju.service.TemplateExplanationService;
import cn.edu.zju.servlet.DispatchServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchingController {

    private static final Logger log = LoggerFactory.getLogger(MatchingController.class);

    private SampleDao sampleDao = new SampleDao();
    private AnnovarDao annovarDao = new AnnovarDao();
    private DrugLabelDao drugLabelDao = new DrugLabelDao();
    private SavedItemDao savedItemDao = new SavedItemDao();
    private ExplanationService explanationService = new GeminiExplanationService(new TemplateExplanationService());

    public void register(DispatchServlet.Dispatcher dispatcher) {
        dispatcher.registerPostMapping("/upload", this::uploadAnnovarOutput);
        dispatcher.registerGetMapping("/matchingIndex", this::matchingIndex);
        dispatcher.registerGetMapping("/matching", this::matching);
        dispatcher.registerGetMapping("/exportMatching", this::exportMatching);
        dispatcher.registerGetMapping("/exportMatchingPdf", this::exportMatchingPdf);
        dispatcher.registerGetMapping("/samples", this::samples);

    }

    public void matchingIndex(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (requireLogin(request, response)) {
            return;
        }
        request.getRequestDispatcher("/views/matching_index.jsp").forward(request, response);
    }

    public void samples(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (requireLogin(request, response)) {
            return;
        }
        List<Sample> samples = sampleDao.findAll();
        request.setAttribute("samples", samples);
        request.getRequestDispatcher("/views/samples.jsp").forward(request, response);
    }

    public void matching(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (requireLogin(request, response)) {
            return;
        }
        Integer sampleId = parseSampleId(request.getParameter("sampleId"));
        if (sampleId == null) {
            request.getRequestDispatcher("/views/samples.jsp").forward(request, response);
            return;
        }
        List<String> refGenes = annovarDao.getRefGenes(sampleId);
        if (refGenes.isEmpty()) {
            response.sendRedirect("samples");
            return;
        }
        List<DrugLabel> drugLabels = drugLabelDao.findAll();
        List<DrugLabel> matched = doMatch(refGenes, drugLabels);
        String username = (String) request.getSession().getAttribute(AuthController.SESSION_USER);
        Set<String> savedMatchedLabelIds = new HashSet<>(savedItemDao.findItemIdsByUsernameAndType(username, SavedItemController.TYPE_MATCH_RESULT));
        Sample sample = sampleDao.findById(sampleId);
        request.setAttribute("matched", matched);
        request.setAttribute("savedMatchedLabelIds", savedMatchedLabelIds);
        request.setAttribute("sample", sample);
        request.setAttribute("refGenes", refGenes);
        if (request.getParameter("explainSample") != null) {
            request.setAttribute("sampleExplanation", explanationService.explainSampleResult(sample, refGenes, matched));
        }
        request.getRequestDispatcher("/views/matching_index_search.jsp").forward(request, response);
    }

    public void exportMatching(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (requireLogin(request, response)) {
            return;
        }
        Integer sampleId = parseSampleId(request.getParameter("sampleId"));
        if (sampleId == null) {
            response.sendRedirect("samples");
            return;
        }

        List<String> refGenes = annovarDao.getRefGenes(sampleId);
        if (refGenes.isEmpty()) {
            response.sendRedirect("matching?sampleId=" + sampleId);
            return;
        }

        List<DrugLabel> drugLabels = drugLabelDao.findAll();
        List<DrugLabel> matched = doMatch(refGenes, drugLabels);

        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"matching_result_sample_" + sampleId + ".csv\"");

        PrintWriter writer = response.getWriter();
        writer.println("sample_id,drug_label_id,label_name,source,summary");
        for (DrugLabel item : matched) {
            writer.println(csv(sampleId.toString()) + ","
                    + csv(item.getId()) + ","
                    + csv(item.getDisplayName()) + ","
                    + csv(item.getSource()) + ","
                    + csv(item.getSummaryMarkdown()));
        }
        writer.flush();
    }

    public void exportMatchingPdf(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (requireLogin(request, response)) {
            return;
        }
        Integer sampleId = parseSampleId(request.getParameter("sampleId"));
        if (sampleId == null) {
            response.sendRedirect("samples");
            return;
        }

        List<String> refGenes = annovarDao.getRefGenes(sampleId);
        if (refGenes.isEmpty()) {
            response.sendRedirect("matching?sampleId=" + sampleId);
            return;
        }

        Sample sample = sampleDao.findById(sampleId);
        List<DrugLabel> matched = doMatch(refGenes, drugLabelDao.findAll());

        request.setAttribute("sample", sample);
        request.setAttribute("refGenes", refGenes);
        request.setAttribute("matched", matched);
        request.setAttribute("sampleExplanation", explanationService.explainSampleResult(sample, refGenes, matched));
        request.getRequestDispatcher("/views/matching_pdf.jsp").forward(request, response);
    }

    private List<DrugLabel> doMatch(List<String> refGenes, List<DrugLabel> drugLabels) {
        List<DrugLabel> matchedLabels = new ArrayList<>();
        for (DrugLabel drugLabel : drugLabels) {
            boolean matched = false;
            for (String gene: refGenes) {
                if (drugLabel.getSummaryMarkdown().contains(gene)) {
                    matched = true;
                }
            }
            if (matched) {
                matchedLabels.add(drugLabel);
            }
        }
        return matchedLabels;
    }

    private Integer parseSampleId(String sampleIdParameter) {
        if (sampleIdParameter == null) {
            return null;
        }
        try {
            return Integer.valueOf(sampleIdParameter);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String csv(String value) {
        if (value == null) {
            return "\"\"";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    public void uploadAnnovarOutput(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (requireLogin(request, response)) {
            return;
        }
        String uploadedBy = request.getParameter("uploaded_by");
        if (uploadedBy == null || uploadedBy.isBlank()) {
            request.setAttribute("validateError", "Uploaded by can not be blank");
            request.getRequestDispatcher("/views/matching_index_error.jsp").forward(request, response);
            return;
        }
        Part requestPart = request.getPart("annovar");
        if (requestPart == null || requestPart.getSize() == 0) {
            request.setAttribute("validateError", "Annovar output file can not be blank");
            request.getRequestDispatcher("/views/matching_index_error.jsp").forward(request, response);
            return;
        }
        InputStream inputStream = requestPart.getInputStream();
        byte[] bytes = inputStream.readAllBytes();
        String content = new String(bytes);

        if (content.isBlank()) {
            request.setAttribute("validateError", "Annovar output file can not be blank");
            request.getRequestDispatcher("/views/matching_index_error.jsp").forward(request, response);
            return;
        }

        int sampleId = sampleDao.save(uploadedBy);
        try {
            annovarDao.save(sampleId, content);
        } catch (ArrayIndexOutOfBoundsException e) {
            request.setAttribute("validateError", "annovar output file is invalid");
            request.getRequestDispatcher("/views/matching_index_error.jsp").forward(request, response);
            return;
        }
        response.sendRedirect("matching?sampleId=" + sampleId);
    }

    private boolean requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession().getAttribute(AuthController.SESSION_USER) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return true;
        }
        return false;
    }
}
