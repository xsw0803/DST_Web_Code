package cn.edu.zju.controller;

import cn.edu.zju.bean.DosingGuideline;
import cn.edu.zju.bean.Drug;
import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.bean.SavedItem;
import cn.edu.zju.dao.DosingGuidelineDao;
import cn.edu.zju.dao.DrugDao;
import cn.edu.zju.dao.DrugLabelDao;
import cn.edu.zju.dao.SavedItemDao;
import cn.edu.zju.servlet.DispatchServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SavedItemController {

    public static final String TYPE_DRUG = "DRUG";
    public static final String TYPE_DRUG_LABEL = "DRUG_LABEL";
    public static final String TYPE_DOSING_GUIDELINE = "DOSING_GUIDELINE";
    public static final String TYPE_MATCH_RESULT = "MATCH_RESULT";

    private final SavedItemDao savedItemDao = new SavedItemDao();
    private final DrugDao drugDao = new DrugDao();
    private final DrugLabelDao drugLabelDao = new DrugLabelDao();
    private final DosingGuidelineDao dosingGuidelineDao = new DosingGuidelineDao();

    public void register(DispatchServlet.Dispatcher dispatcher) {
        dispatcher.registerGetMapping("/savedItems", this::savedItems);
        dispatcher.registerGetMapping("/saveItem", this::saveItem);
        dispatcher.registerGetMapping("/removeSavedItem", this::removeSavedItem);
    }

    public void savedItems(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (requireLogin(request, response)) {
            return;
        }
        String username = (String) request.getSession().getAttribute(AuthController.SESSION_USER);
        List<SavedItem> savedItems = enrich(savedItemDao.findByUsername(username));
        request.setAttribute("savedItems", savedItems);
        request.getRequestDispatcher("/views/saved_items.jsp").forward(request, response);
    }

    public void saveItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (requireLogin(request, response)) {
            return;
        }
        String username = (String) request.getSession().getAttribute(AuthController.SESSION_USER);
        String itemType = trimToNull(request.getParameter("itemType"));
        String itemId = trimToNull(request.getParameter("itemId"));
        if (itemType != null && itemId != null) {
            savedItemDao.save(username, itemType, itemId);
        }
        response.sendRedirect(resolveReturnUrl(request));
    }

    public void removeSavedItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (requireLogin(request, response)) {
            return;
        }
        String username = (String) request.getSession().getAttribute(AuthController.SESSION_USER);
        String itemType = trimToNull(request.getParameter("itemType"));
        String itemId = trimToNull(request.getParameter("itemId"));
        if (itemType != null && itemId != null) {
            savedItemDao.delete(username, itemType, itemId);
        }
        response.sendRedirect(resolveReturnUrl(request));
    }

    private List<SavedItem> enrich(List<SavedItem> rawItems) {
        List<SavedItem> items = new ArrayList<>();
        for (SavedItem item : rawItems) {
            if (TYPE_DRUG.equals(item.getItemType())) {
                Drug drug = drugDao.findById(item.getItemId());
                if (drug != null) {
                    item.setDisplayName(drug.getName());
                    item.setDetailUrl("drugDetail?id=" + drug.getId());
                    item.setTypeLabel("Drug");
                    items.add(item);
                }
            } else if (TYPE_DRUG_LABEL.equals(item.getItemType()) || TYPE_MATCH_RESULT.equals(item.getItemType())) {
                DrugLabel drugLabel = drugLabelDao.findById(item.getItemId());
                if (drugLabel != null) {
                    item.setDisplayName(drugLabel.getDisplayName());
                    item.setDetailUrl("drugLabelDetail?id=" + drugLabel.getId());
                    item.setTypeLabel(TYPE_MATCH_RESULT.equals(item.getItemType()) ? "Matched Result" : "Drug Label");
                    items.add(item);
                }
            } else if (TYPE_DOSING_GUIDELINE.equals(item.getItemType())) {
                DosingGuideline guideline = dosingGuidelineDao.findById(item.getItemId());
                if (guideline != null) {
                    item.setDisplayName(guideline.getName());
                    item.setDetailUrl("dosingGuidelineDetail?id=" + guideline.getId());
                    item.setTypeLabel("Dosing Guideline");
                    items.add(item);
                }
            }
        }
        return items;
    }

    private String resolveReturnUrl(HttpServletRequest request) {
        String returnTo = trimToNull(request.getParameter("returnTo"));
        if (returnTo != null) {
            return request.getContextPath() + returnTo;
        }
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank()) {
            return referer;
        }
        return request.getContextPath() + "/savedItems";
    }

    private boolean requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession().getAttribute(AuthController.SESSION_USER) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return true;
        }
        return false;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
