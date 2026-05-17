package cn.edu.zju.servlet;

import cn.edu.zju.bean.AnnovarVariant;
import cn.edu.zju.dao.AnnovarDao;
import cn.edu.zju.dao.SampleDao;
import cn.edu.zju.filter.AuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "MutationUploadServlet", urlPatterns = "/mutationUpload")
@MultipartConfig
public class MutationUploadServlet extends HttpServlet {

    private static final String[] REQUIRED_COLUMNS = new String[]{
            "Chr", "Start", "End", "Ref", "Alt",
            "Func.refGene", "Gene.refGene", "ExonicFunc.refGene", "AAChange.refGene", "avsnp150"
    };

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/mutation_upload.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("annovarFile");
        if (filePart == null || filePart.getSize() == 0) {
            request.setAttribute("error", "Please choose an ANNOVAR annotation file first.");
            request.getRequestDispatcher("/views/mutation_upload.jsp").forward(request, response);
            return;
        }
        String originalFileName = extractFileName(filePart);
        if (!isSupportedFileName(originalFileName)) {
            request.setAttribute("error", "Upload failed: please upload a .tsv or .txt ANNOVAR-style annotation file.");
            request.getRequestDispatcher("/views/mutation_upload.jsp").forward(request, response);
            return;
        }

        List<AnnovarVariant> variants;
        try {
            variants = parseVariants(filePart);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/views/mutation_upload.jsp").forward(request, response);
            return;
        }

        if (variants.isEmpty()) {
            request.setAttribute("error", "No valid variant rows were found in uploaded file.");
            request.getRequestDispatcher("/views/mutation_upload.jsp").forward(request, response);
            return;
        }

        Object username = request.getSession().getAttribute(AuthenticationFilter.SESSION_USERNAME);
        String uploadedBy = username == null ? "anonymous" : username.toString();
        SampleDao sampleDao = new SampleDao();
        int sampleId = sampleDao.save(uploadedBy, originalFileName);
        if (sampleId <= 0) {
            request.setAttribute("error", "Failed to create sample record.");
            request.getRequestDispatcher("/views/mutation_upload.jsp").forward(request, response);
            return;
        }

        AnnovarDao annovarDao = new AnnovarDao();
        boolean saved = annovarDao.saveMinimalVariants(sampleId, variants);
        if (!saved) {
            request.setAttribute("error", "Upload failed: unable to store parsed variants into database.");
            request.getRequestDispatcher("/views/mutation_upload.jsp").forward(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/mutationResult?sampleId=" + sampleId);
    }

    private List<AnnovarVariant> parseVariants(Part filePart) throws IOException {
        List<AnnovarVariant> variants = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(filePart.getInputStream(), StandardCharsets.UTF_8))) {
            String headerLine = reader.readLine();
            if (headerLine == null || headerLine.trim().isEmpty()) {
                throw new IllegalArgumentException("Uploaded file is empty.");
            }

            String delimiter = headerLine.contains("\t") ? "\t" : null;
            if (delimiter == null) {
                throw new IllegalArgumentException("Upload failed: the file is not tab-separated. Please upload an ANNOVAR-style TSV file.");
            }
            String[] headers = headerLine.split(delimiter, -1);
            Map<String, Integer> headerIndex = buildHeaderIndex(headers);
            validateRequiredColumns(headerIndex);
            int minimumColumns = resolveMinimumRequiredColumns(headerIndex);

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] split = line.split(delimiter, -1);
                if (split.length < minimumColumns) {
                    throw new IllegalArgumentException("Upload failed: row " + lineNumber + " has fewer columns than expected.");
                }
                AnnovarVariant variant = buildVariant(split, headerIndex);
                if (variant != null) {
                    variants.add(variant);
                }
            }
        }
        return variants;
    }

    private Map<String, Integer> buildHeaderIndex(String[] headers) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            map.put(headers[i].trim(), i);
        }
        if (map.containsKey("#Chr")) {
            map.put("Chr", map.get("#Chr"));
        }
        return map;
    }

    private void validateRequiredColumns(Map<String, Integer> headerIndex) {
        List<String> missing = new ArrayList<>();
        for (String column : REQUIRED_COLUMNS) {
            if (!headerIndex.containsKey(column)) {
                missing.add(column);
            }
        }
        if (!missing.isEmpty()) {
            throw new IllegalArgumentException("Upload failed: missing required ANNOVAR columns: " + String.join(", ", missing) + ".");
        }
    }

    private int resolveMinimumRequiredColumns(Map<String, Integer> headerIndex) {
        int min = 0;
        for (String column : REQUIRED_COLUMNS) {
            Integer idx = headerIndex.get(column);
            if (idx != null && idx + 1 > min) {
                min = idx + 1;
            }
        }
        return min;
    }

    private AnnovarVariant buildVariant(String[] split, Map<String, Integer> headerIndex) {
        String chr = getValue(split, headerIndex, "Chr");
        String start = getValue(split, headerIndex, "Start");
        String end = getValue(split, headerIndex, "End");
        String ref = getValue(split, headerIndex, "Ref");
        String alt = getValue(split, headerIndex, "Alt");

        if (isBlank(chr) || isBlank(start) || isBlank(end) || isBlank(ref) || isBlank(alt)) {
            return null;
        }

        AnnovarVariant variant = new AnnovarVariant();
        variant.setChr(chr);
        variant.setStart(start);
        variant.setEnd(end);
        variant.setRef(ref);
        variant.setAlt(alt);
        variant.setFuncRefGene(getValue(split, headerIndex, "Func.refGene"));
        variant.setGeneRefGene(getValue(split, headerIndex, "Gene.refGene"));
        variant.setExonicFuncRefGene(getValue(split, headerIndex, "ExonicFunc.refGene"));
        variant.setAaChangeRefGene(getValue(split, headerIndex, "AAChange.refGene"));
        variant.setAvsnp150(getValue(split, headerIndex, "avsnp150"));
        return variant;
    }

    private String getValue(String[] split, Map<String, Integer> headerIndex, String column) {
        Integer index = headerIndex.get(column);
        if (index == null || index < 0 || index >= split.length) {
            return "";
        }
        return split[index].trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isSupportedFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        String lower = fileName.toLowerCase();
        return lower.endsWith(".tsv") || lower.endsWith(".txt");
    }

    private String extractFileName(Part filePart) {
        String submitted = filePart.getSubmittedFileName();
        if (submitted == null) {
            return "";
        }
        int slash = Math.max(submitted.lastIndexOf('/'), submitted.lastIndexOf('\\'));
        return slash >= 0 ? submitted.substring(slash + 1) : submitted;
    }
}
