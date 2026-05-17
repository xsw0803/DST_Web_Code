package cn.edu.zju.service;

import cn.edu.zju.AppConfig;
import cn.edu.zju.bean.DosingGuideline;
import cn.edu.zju.bean.Drug;
import cn.edu.zju.bean.DrugLabel;
import cn.edu.zju.bean.Sample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.StringJoiner;

public class GeminiExplanationService implements ExplanationService {

    private static final Logger log = LoggerFactory.getLogger(GeminiExplanationService.class);

    private final ExplanationService fallback;
    private final HttpClient httpClient;
    private final AppConfig appConfig;

    public GeminiExplanationService(ExplanationService fallback) {
        this.fallback = fallback;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.appConfig = AppConfig.getInstance();
    }

    @Override
    public String explainDrug(Drug drug, int relatedLabelCount, int relatedGuidelineCount) {
        String prompt = "Explain this pharmacogenomics drug record in 2 short plain-language sentences for educational use only. " +
                "Drug name: " + safe(drug.getName()) + ". Biomarker flag: " + drug.isBiomarker() +
                ". Related drug labels: " + relatedLabelCount + ". Related dosing guidelines: " + relatedGuidelineCount + ".";
        return explain(prompt, fallback.explainDrug(drug, relatedLabelCount, relatedGuidelineCount));
    }

    @Override
    public String explainDrugLabel(DrugLabel drugLabel) {
        String prompt = "Explain this pharmacogenomics drug label in 2 short plain-language sentences for educational use only. " +
                "Label name: " + safe(drugLabel.getDisplayName()) + ". Source: " + safe(drugLabel.getSource()) +
                ". Drug ID: " + safe(drugLabel.getDrugId()) + ". Summary: " + safe(drugLabel.getSummaryMarkdown()) + ".";
        return explain(prompt, fallback.explainDrugLabel(drugLabel));
    }

    @Override
    public String explainDosingGuideline(DosingGuideline dosingGuideline) {
        String prompt = "Explain this pharmacogenomics dosing guideline in 2 short plain-language sentences for educational use only. " +
                "Guideline name: " + safe(dosingGuideline.getName()) + ". Source: " + safe(dosingGuideline.getSource()) +
                ". Drug ID: " + safe(dosingGuideline.getDrugId()) + ". Summary: " + safe(dosingGuideline.getSummaryMarkdown()) + ".";
        return explain(prompt, fallback.explainDosingGuideline(dosingGuideline));
    }

    @Override
    public String explainSampleResult(Sample sample, List<String> refGenes, List<DrugLabel> matchedLabels) {
        StringJoiner geneJoiner = new StringJoiner(", ");
        for (int i = 0; i < refGenes.size() && i < 8; i++) {
            geneJoiner.add(refGenes.get(i));
        }
        StringJoiner labelJoiner = new StringJoiner(", ");
        for (int i = 0; i < matchedLabels.size() && i < 5; i++) {
            labelJoiner.add(matchedLabels.get(i).getDisplayName());
        }
        String prompt = "Explain this pharmacogenomics matching result for a user in 3 short plain-language sentences for educational use only. " +
                "Sample ID: " + sample.getId() + ". Uploaded by: " + safe(sample.getUploadedBy()) + ". " +
                "Detected genes: " + safe(geneJoiner.toString()) + ". " +
                "Matched label count: " + matchedLabels.size() + ". " +
                "Example matched labels: " + safe(labelJoiner.toString()) + ". " +
                "Focus on what the result means, why genes may connect to label results, and what the user should review next.";
        return explain(prompt, fallback.explainSampleResult(sample, refGenes, matchedLabels));
    }

    private String explain(String prompt, String fallbackText) {
        if (!appConfig.isAiEnabled()) {
            return fallbackText;
        }
        String apiKey = trimToNull(appConfig.getGeminiApiKey());
        String model = trimToNull(appConfig.getGeminiModel());
        if (apiKey == null || model == null) {
            return fallbackText;
        }

        String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                + URLEncoder.encode(model, StandardCharsets.UTF_8)
                + ":generateContent?key="
                + URLEncoder.encode(apiKey, StandardCharsets.UTF_8);

        String payload = "{"
                + "\"contents\":[{"
                + "\"parts\":[{"
                + "\"text\":\"" + escapeJson(prompt) + "\""
                + "}]"
                + "}]"
                + "}";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(20))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                String text = extractText(response.body());
                if (text != null && !text.isBlank()) {
                    return text;
                }
            } else {
                log.info("Gemini explanation request failed with status {}", response.statusCode());
            }
        } catch (InterruptedException e) {
            log.info("Gemini explanation request failed", e);
            Thread.currentThread().interrupt();
        } catch (IOException | IllegalArgumentException e) {
            log.info("Gemini explanation request failed", e);
        }
        return fallbackText;
    }

    private String extractText(String json) {
        String marker = "\"text\": \"";
        int start = json.indexOf(marker);
        if (start < 0) {
            marker = "\"text\":\"";
            start = json.indexOf(marker);
        }
        if (start < 0) {
            return null;
        }
        start += marker.length();
        StringBuilder builder = new StringBuilder();
        boolean escaping = false;
        for (int i = start; i < json.length(); i++) {
            char current = json.charAt(i);
            if (escaping) {
                switch (current) {
                    case 'n':
                        builder.append('\n');
                        break;
                    case 't':
                        builder.append('\t');
                        break;
                    case 'r':
                        builder.append('\r');
                        break;
                    case '"':
                    case '\\':
                    case '/':
                        builder.append(current);
                        break;
                    default:
                        builder.append(current);
                        break;
                }
                escaping = false;
                continue;
            }
            if (current == '\\') {
                escaping = true;
                continue;
            }
            if (current == '"') {
                break;
            }
            builder.append(current);
        }
        return builder.toString().trim();
    }

    private String escapeJson(String value) {
        return safe(value)
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
