package cn.edu.zju.controller;

import cn.edu.zju.bean.DosingGuideline;
import cn.edu.zju.bean.Drug;
import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.dao.DosingGuidelineDao;
import cn.edu.zju.dao.DrugDao;
import cn.edu.zju.dao.DrugLabelDao;
import cn.edu.zju.dao.SavedItemDao;
import cn.edu.zju.service.ExplanationService;
import cn.edu.zju.service.GeminiExplanationService;
import cn.edu.zju.service.TemplateExplanationService;
import cn.edu.zju.servlet.DispatchServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class KnowledgeBaseController {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseController.class);

    private DrugDao drugDao = new DrugDao();
    private DrugLabelDao drugLabelDao = new DrugLabelDao();
    private DosingGuidelineDao dosingGuidelineDao = new DosingGuidelineDao();
    private SavedItemDao savedItemDao = new SavedItemDao();
    private ExplanationService explanationService = new GeminiExplanationService(new TemplateExplanationService());

    public void register(DispatchServlet.Dispatcher dispatcher) {
        dispatcher.registerGetMapping("/drugs", this::drugs);
        dispatcher.registerGetMapping("/drugLabels", this::drugLabels);
        dispatcher.registerGetMapping("/dosingGuideline", this::dosingGuideline);
        dispatcher.registerGetMapping("/drugDetail", this::drugDetail);
        dispatcher.registerGetMapping("/drugLabelDetail", this::drugLabelDetail);
        dispatcher.registerGetMapping("/dosingGuidelineDetail", this::dosingGuidelineDetail);
    }

    public void drugs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (requireLogin(request, response)) {
            return;
        }
        String keyword = request.getParameter("keyword");

        List<Drug> drugs;
        if (keyword == null || keyword.isBlank()) {
            drugs = drugDao.findAll();
        } else {
            drugs = drugDao.searchByName(keyword.trim());
        }

        request.setAttribute("drugs", drugs);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/views/drugs.jsp").forward(request, response);
    }

    public void drugLabels(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (requireLogin(request, response)) {
            return;
        }
        String keyword = request.getParameter("keyword");

        List<DrugLabel> drugLabels;
        if (keyword == null || keyword.isBlank()) {
            drugLabels = drugLabelDao.findAll();
        } else {
            drugLabels = drugLabelDao.search(keyword.trim());
        }

        request.setAttribute("drugLabels", drugLabels);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/views/drug_labels.jsp").forward(request, response);
    }

    public void dosingGuideline(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (requireLogin(request, response)) {
            return;
        }
        String keyword = request.getParameter("keyword");

        List<DosingGuideline> dosingGuidelines;
        if (keyword == null || keyword.isBlank()) {
            dosingGuidelines = dosingGuidelineDao.findAll();
        } else {
            dosingGuidelines = dosingGuidelineDao.search(keyword.trim());
        }

        request.setAttribute("dosingGuidelines", dosingGuidelines);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/views/dosing_guideline.jsp").forward(request, response);
    }

    public void drugDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (requireLogin(request, response)) {
            return;
        }
        String id = request.getParameter("id");
        if (id == null || id.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/drugs");
            return;
        }

        Drug drug = drugDao.findById(id.trim());
        if (drug == null) {
            response.sendRedirect(request.getContextPath() + "/drugs");
            return;
        }

        List<DrugLabel> relatedDrugLabels = drugLabelDao.findByDrugId(drug.getId());
        List<DosingGuideline> relatedDosingGuidelines = dosingGuidelineDao.findByDrugId(drug.getId());
        String username = (String) request.getSession().getAttribute(AuthController.SESSION_USER);
        request.setAttribute("drug", drug);
        request.setAttribute("relatedDrugLabels", relatedDrugLabels);
        request.setAttribute("relatedDosingGuidelines", relatedDosingGuidelines);
        request.setAttribute("isSaved", savedItemDao.exists(username, SavedItemController.TYPE_DRUG, drug.getId()));
        if (request.getParameter("explain") != null) {
            request.setAttribute("explanation", explanationService.explainDrug(drug, relatedDrugLabels.size(), relatedDosingGuidelines.size()));
        }
        request.getRequestDispatcher("/views/drug_detail.jsp").forward(request, response);
    }

    public void drugLabelDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (requireLogin(request, response)) {
            return;
        }
        String id = request.getParameter("id");
        if (id == null || id.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/drugLabels");
            return;
        }

        DrugLabel drugLabel = drugLabelDao.findById(id.trim());
        if (drugLabel == null) {
            response.sendRedirect(request.getContextPath() + "/drugLabels");
            return;
        }

        Drug relatedDrug = drugDao.findById(drugLabel.getDrugId());
        List<DosingGuideline> relatedDosingGuidelines = dosingGuidelineDao.findByDrugId(drugLabel.getDrugId());
        String username = (String) request.getSession().getAttribute(AuthController.SESSION_USER);
        boolean savedAsLabel = savedItemDao.exists(username, SavedItemController.TYPE_DRUG_LABEL, drugLabel.getId());
        boolean savedAsMatchResult = savedItemDao.exists(username, SavedItemController.TYPE_MATCH_RESULT, drugLabel.getId());
        boolean returnToMatching = "matching".equals(request.getParameter("returnTo"));
        request.setAttribute("drugLabel", drugLabel);
        request.setAttribute("relatedDrug", relatedDrug);
        request.setAttribute("relatedDosingGuidelines", relatedDosingGuidelines);
        request.setAttribute("isSaved", savedAsLabel || savedAsMatchResult);
        request.setAttribute("savedItemType", savedAsMatchResult ? SavedItemController.TYPE_MATCH_RESULT : SavedItemController.TYPE_DRUG_LABEL);
        request.setAttribute("saveItemType", returnToMatching ? SavedItemController.TYPE_MATCH_RESULT : SavedItemController.TYPE_DRUG_LABEL);
        if (request.getParameter("explain") != null) {
            request.setAttribute("explanation", explanationService.explainDrugLabel(drugLabel));
        }
        request.setAttribute("returnToMatching", returnToMatching);
        request.setAttribute("sampleId", request.getParameter("sampleId"));
        request.getRequestDispatcher("/views/drug_label_detail.jsp").forward(request, response);
    }

    public void dosingGuidelineDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (requireLogin(request, response)) {
            return;
        }
        String id = request.getParameter("id");
        if (id == null || id.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/dosingGuideline");
            return;
        }

        DosingGuideline dosingGuideline = dosingGuidelineDao.findById(id.trim());
        if (dosingGuideline == null) {
            response.sendRedirect(request.getContextPath() + "/dosingGuideline");
            return;
        }

        Drug relatedDrug = drugDao.findById(dosingGuideline.getDrugId());
        List<DrugLabel> relatedDrugLabels = drugLabelDao.findByDrugId(dosingGuideline.getDrugId());
        String username = (String) request.getSession().getAttribute(AuthController.SESSION_USER);
        request.setAttribute("dosingGuideline", dosingGuideline);
        request.setAttribute("relatedDrug", relatedDrug);
        request.setAttribute("relatedDrugLabels", relatedDrugLabels);
        request.setAttribute("isSaved", savedItemDao.exists(username, SavedItemController.TYPE_DOSING_GUIDELINE, dosingGuideline.getId()));
        if (request.getParameter("explain") != null) {
            request.setAttribute("explanation", explanationService.explainDosingGuideline(dosingGuideline));
        }
        request.getRequestDispatcher("/views/dosing_guideline_detail.jsp").forward(request, response);
    }

    private boolean requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession().getAttribute(AuthController.SESSION_USER) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return true;
        }
        return false;
    }
}
