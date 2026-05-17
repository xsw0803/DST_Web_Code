<%--
  Created by IntelliJ IDEA.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Drug Evidence</title>
    <link href="<%=request.getContextPath()%>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="<%=request.getContextPath()%>/static/jquery/jquery-3.4.1.js"></script>
    <script src="<%=request.getContextPath()%>/static/bootstrap/js/bootstrap.bundle.min.js"></script>
    <link href="<%=request.getContextPath()%>/static/css/app.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/static/css/style.css" rel="stylesheet">
</head>
<body>
<jsp:include page="head.jsp" />

<div class="container-fluid">
    <div class="row">
        <jsp:include page="nav.jsp" >
            <jsp:param name="active" value="drugs" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>
                    <c:choose>
                        <c:when test="${not empty drug}">
                            Drug Evidence: ${drug.name}
                        </c:when>
                        <c:otherwise>
                            Drug Evidence
                        </c:otherwise>
                    </c:choose>
                </h2>
            </div>
            <p class="page-description">Integrated evidence page for drug information, regulatory label annotations, and pharmacogenetic dosing guidelines.</p>
            <a class="btn btn-sm btn-outline-primary mb-3" href="<%=request.getContextPath()%>/drugs">Back to Drugs</a>

            <c:if test="${empty drug}">
                <div class="alert alert-warning">
                    No drug found for ID: ${drugId}
                </div>
            </c:if>

            <c:if test="${not empty drug}">
                <c:set var="labelCount" value="${fn:length(drugLabels)}" />
                <c:set var="guidelineCount" value="${fn:length(dosingGuidelines)}" />

                <div class="mb-3 evidence-jump-links">
                    Jump to:
                    <a href="#summarySection">Drug Summary</a>
                    <a href="#labelEvidenceSection">Drug Labels</a>
                    <a href="#guidelineEvidenceSection">Dosing Guidelines</a>
                    <a href="#disclaimerSection">Disclaimer</a>
                </div>

                <h4 id="summarySection" class="mt-2">1. Drug Summary</h4>
                <div class="evidence-summary-card mb-4">
                    <div class="row">
                        <div class="col-md-6 mb-2 mb-md-0">
                            <div class="summary-item"><span class="summary-label">Drug Name</span><span class="summary-value">${drug.name}</span></div>
                            <div class="summary-item"><span class="summary-label">Drug ID</span><span class="summary-value">${drug.id}</span></div>
                            <div class="summary-item">
                                <span class="summary-label">Biomarker</span>
                                <span class="summary-value">
                                    <span class="badge ${drug.biomarker ? 'badge-success' : 'badge-secondary'}">
                                        ${drug.biomarker ? 'Available' : 'Not available'}
                                    </span>
                                </span>
                            </div>
                            <div class="summary-item">
                                <span class="summary-label">PharmGKB</span>
                                <span class="summary-value">
                                    <c:choose>
                                        <c:when test="${not empty drug.drugUrl}">
                                            <a href="https://www.pharmgkb.org${drug.drugUrl}" target="_blank">Open PharmGKB record</a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">Not available</span>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="summary-item"><span class="summary-label">Drug Label Evidence</span><span class="summary-value">${labelCount} record(s)</span></div>
                            <div class="summary-item"><span class="summary-label">Dosing Guideline Evidence</span><span class="summary-value">${guidelineCount} record(s)</span></div>
                            <div class="summary-item"><span class="summary-label">Page Type</span><span class="summary-value">Integrated evidence archive</span></div>
                        </div>
                    </div>
                </div>

                <h5 class="mt-1 mb-2">Evidence Overview</h5>
                <div class="evidence-summary-card mb-4">
                    <c:choose>
                        <c:when test="${drug.biomarker}">
                            This drug has biomarker-related pharmacogenetic evidence.
                            Current records include <strong>${labelCount}</strong> drug label entries and <strong>${guidelineCount}</strong> dosing guideline entries.
                            Review source-specific interpretations below.
                        </c:when>
                        <c:otherwise>
                            This drug currently has no confirmed biomarker availability flag in the drug table.
                            Label and guideline records are still provided for reference and professional interpretation.
                        </c:otherwise>
                    </c:choose>
                </div>

                <h4 id="labelEvidenceSection" class="mt-4">2. Drug Label Evidence</h4>
                <c:if test="${empty drugLabels}">
                    <div class="alert alert-light border">No drug label information available for this drug.</div>
                </c:if>
                <c:if test="${not empty drugLabels}">
                    <c:forEach items="${drugLabels}" var="item">
                        <c:set var="labelSummary" value="${empty item.summaryMarkdown ? 'No summary available for this label record.' : item.summaryMarkdown}" />
                        <div class="evidence-record-card mb-3">
                            <div class="d-flex flex-wrap justify-content-between align-items-start mb-2">
                                <h5 class="mb-1">Source: ${item.source}</h5>
                                <span class="badge ${item.dosingInformation ? 'badge-success' : 'badge-secondary'}">
                                    ${item.dosingInformation ? 'Dosing information available' : 'Dosing information not available'}
                                </span>
                            </div>
                            <div class="evidence-meta mb-2">
                                <strong>Label ID:</strong> ${item.id}
                            </div>
                            <div class="evidence-summary-title">Label Summary</div>
                            <c:choose>
                                <c:when test="${fn:length(labelSummary) > 420}">
                                    <p class="evidence-summary-text">${fn:substring(labelSummary, 0, 420)}...</p>
                                    <details class="evidence-details">
                                        <summary>Show full summary</summary>
                                        <p class="evidence-summary-text mb-0">${labelSummary}</p>
                                    </details>
                                </c:when>
                                <c:otherwise>
                                    <p class="evidence-summary-text mb-0">${labelSummary}</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:forEach>
                </c:if>

                <h4 id="guidelineEvidenceSection" class="mt-4">3. Dosing Guideline Evidence</h4>
                <c:if test="${empty dosingGuidelines}">
                    <div class="alert alert-light border">No dosing guideline available for this drug.</div>
                </c:if>
                <c:if test="${not empty dosingGuidelines}">
                    <c:forEach items="${dosingGuidelines}" var="item">
                        <c:set var="guidelineSummary" value="${empty item.summaryMarkdown ? 'No summary available for this guideline record.' : item.summaryMarkdown}" />
                        <div class="evidence-record-card mb-3">
                            <div class="d-flex flex-wrap justify-content-between align-items-start mb-2">
                                <h5 class="mb-1">Guideline: ${item.name}</h5>
                                <span class="badge ${item.recommendation ? 'badge-success' : 'badge-secondary'}">
                                    ${item.recommendation ? 'Recommendation available' : 'No specific recommendation'}
                                </span>
                            </div>
                            <div class="evidence-meta mb-2">
                                <strong>Guideline ID:</strong> ${item.id}
                                <span class="mx-2">|</span>
                                <strong>Source:</strong> ${item.source}
                            </div>
                            <div class="evidence-summary-title">Guideline Summary</div>
                            <c:choose>
                                <c:when test="${fn:length(guidelineSummary) > 420}">
                                    <p class="evidence-summary-text">${fn:substring(guidelineSummary, 0, 420)}...</p>
                                    <details class="evidence-details">
                                        <summary>Show full summary</summary>
                                        <p class="evidence-summary-text mb-0">${guidelineSummary}</p>
                                    </details>
                                </c:when>
                                <c:otherwise>
                                    <p class="evidence-summary-text mb-0">${guidelineSummary}</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:forEach>
                </c:if>
            </c:if>

            <div id="disclaimerSection" class="mt-4 disclaimer-box">
                This integrated drug evidence page is for educational and research reference only and does not provide medical advice.
            </div>
        </main>
    </div>
</div>
</body>
</html>
