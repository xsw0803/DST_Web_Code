package cn.edu.zju.servlet;

import cn.edu.zju.bean.DosingGuideline;
import cn.edu.zju.bean.Drug;
import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.dao.DosingGuidelineDao;
import cn.edu.zju.dao.DrugDao;
import cn.edu.zju.dao.DrugLabelDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "DrugDetailServlet", urlPatterns = "/drugDetail")
public class DrugDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        String drugId = idParam == null ? "" : idParam.trim();

        DrugDao drugDao = new DrugDao();
        DrugLabelDao drugLabelDao = new DrugLabelDao();
        DosingGuidelineDao dosingGuidelineDao = new DosingGuidelineDao();

        Drug drug = drugDao.findById(drugId);
        List<DrugLabel> drugLabels = Collections.emptyList();
        List<DosingGuideline> dosingGuidelines = Collections.emptyList();

        if (drug != null) {
            drugLabels = drugLabelDao.findByDrugId(drugId);
            dosingGuidelines = dosingGuidelineDao.findByDrugId(drugId);
        }

        request.setAttribute("drug", drug);
        request.setAttribute("drugId", drugId);
        request.setAttribute("drugLabels", drugLabels);
        request.setAttribute("dosingGuidelines", dosingGuidelines);
        request.getRequestDispatcher("/views/drug_detail.jsp").forward(request, response);
    }
}
