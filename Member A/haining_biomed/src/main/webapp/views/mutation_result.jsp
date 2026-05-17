<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Mutation Result</title>
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
            <jsp:param name="active" value="mutation_upload" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>Mutation Matching Result</h2>
            </div>
            <p class="page-description">This page summarizes matched pharmacogenetic evidence from the uploaded annotation file and provides role-based interpretation.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <c:if test="${empty error}">
                <h4>1. Sample Summary</h4>
                <div class="summary-panel mb-3">
                    <div class="row">
                        <div class="col-md-4 col-lg-2 mb-2">
                            <div class="summary-metric-card">
                                <div class="metric-label">Sample ID</div>
                                <div class="metric-value">${sampleSummary.sampleId}</div>
                                <div class="metric-note">Numeric ID: ${sampleId}</div>
                            </div>
                        </div>
                        <div class="col-md-4 col-lg-2 mb-2">
                            <div class="summary-metric-card">
                                <div class="metric-label">Total Variants</div>
                                <div class="metric-value">${sampleSummary.totalVariants}</div>
                            </div>
                        </div>
                        <div class="col-md-4 col-lg-2 mb-2">
                            <div class="summary-metric-card">
                                <div class="metric-label">Potentially Relevant Variants</div>
                                <div class="metric-value">${sampleSummary.functionalVariantCount}</div>
                            </div>
                        </div>
                        <div class="col-md-4 col-lg-2 mb-2">
                            <div class="summary-metric-card">
                                <div class="metric-label">Matched Genes</div>
                                <div class="metric-value">${sampleSummary.matchedGeneCount}</div>
                            </div>
                        </div>
                        <div class="col-md-4 col-lg-2 mb-2">
                            <div class="summary-metric-card">
                                <div class="metric-label">Matched Drugs</div>
                                <div class="metric-value">${sampleSummary.matchedDrugCount}</div>
                            </div>
                        </div>
                        <div class="col-md-4 col-lg-2 mb-2">
                            <div class="summary-metric-card">
                                <div class="metric-label">Matching Method</div>
                                <div class="metric-value metric-value-small">${sampleSummary.matchingMethod}</div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="mb-3 result-action-row">
                    <a class="btn btn-outline-primary btn-sm mr-2 mb-2" href="<%=request.getContextPath()%>/mutationExport?sampleId=${sampleId}&format=csv">
                        Export CSV
                    </a>
                    <a class="btn btn-outline-secondary btn-sm mr-2 mb-2" href="<%=request.getContextPath()%>/mutationExport?sampleId=${sampleId}&format=txt">
                        Download Summary
                    </a>
                    <a class="btn btn-outline-secondary btn-sm mr-2 mb-2" href="<%=request.getContextPath()%>/mutationUpload">Back to Upload</a>
                    <c:if test="${canViewSampleHistory}">
                        <a class="btn btn-outline-secondary btn-sm mb-2" href="<%=request.getContextPath()%>/sampleHistory">View Sample History</a>
                    </c:if>
                </div>

                <c:if test="${canViewProfessional}">
                    <div class="mb-3">
                        <strong>View Mode:</strong>
                        <label class="ml-2 mr-3">
                            <input type="radio" name="viewMode" value="patient" checked> Patient View
                        </label>
                        <label class="mr-3">
                            <input type="radio" name="viewMode" value="professional"> Professional View
                        </label>
                        <label>
                            <input type="radio" name="viewMode" value="both"> Both
                        </label>
                    </div>
                </c:if>
                <c:if test="${not canViewProfessional}">
                    <div class="alert alert-light border mb-3 role-note">
                        Patient view is active. Detailed variant annotations and matching evidence are available to professional users.
                    </div>
                </c:if>

                <div id="patientViewSection">
                    <h4>2. Simplified Interpretation</h4>
                    <c:if test="${empty simplifiedCards}">
                        <div class="alert alert-light border">No matched drug interpretation found for this sample.</div>
                    </c:if>
                    <c:if test="${not empty simplifiedCards}">
                        <div class="row">
                            <c:forEach items="${simplifiedCards}" var="card">
                                <div class="col-md-6 mb-3">
                                    <div class="card h-100 patient-result-card">
                                        <div class="card-body">
                                            <c:set var="matchingBadgeClass" value="badge-secondary" />
                                            <c:if test="${card.matchingLevel == 'Variant-level match'}">
                                                <c:set var="matchingBadgeClass" value="badge-success" />
                                            </c:if>
                                            <c:if test="${card.matchingLevel == 'Gene-level match'}">
                                                <c:set var="matchingBadgeClass" value="badge-warning" />
                                            </c:if>
                                            <div class="d-flex justify-content-between align-items-start">
                                                <h5 class="card-title mb-2">
                                                    ${card.drugName}
                                                    <c:if test="${not empty card.drugId}">
                                                        <small class="text-muted">(${card.drugId})</small>
                                                    </c:if>
                                                </h5>
                                                <span class="badge ${matchingBadgeClass}">
                                                    ${card.matchingLevel}
                                                </span>
                                            </div>
                                            <p class="card-text mb-1"><strong>Related gene:</strong> ${card.relatedGenes}</p>
                                            <p class="card-text mb-1">
                                                <strong>Guideline status:</strong>
                                                <span class="badge ${card.guidelineAvailable ? 'badge-success' : 'badge-secondary'}">
                                                    ${card.guidelineAvailable ? 'Available' : 'Not available'}
                                                </span>
                                            </p>
                                            <p class="card-text mb-1"><strong>Evidence basis:</strong> ${card.evidenceBasis}</p>
                                            <p class="card-text mb-1"><strong>Possible implication:</strong> ${card.possibleImplication}</p>
                                            <p class="card-text"><strong>Advice:</strong> ${card.advice}</p>
                                            <c:if test="${card.viewEvidenceEnabled}">
                                                <a class="btn btn-outline-primary btn-sm evidence-btn mt-2" href="<%=request.getContextPath()%>/drugDetail?id=${card.drugId}">
                                                    View Drug Evidence
                                                </a>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>
                </div>

                <c:if test="${canViewProfessional}">
                <div id="professionalViewSection" style="display:none;">
                    <h4 class="mt-4">3. Professional Details</h4>
                    <p class="page-description mb-2">Professional details provide variant annotations, matching basis, and source-level evidence for traceability.</p>

                    <h5 class="mt-3">A. Variant Evidence</h5>
                    <div class="table-responsive">
                        <table class="table table-striped table-sm professional-table">
                            <thead>
                            <tr>
                                <th>Gene</th>
                                <th>rsID</th>
                                <th>Location</th>
                                <th>Ref / Alt</th>
                                <th>Function</th>
                                <th>Functional Status</th>
                                <th>Match Status</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${allVariants}" var="item">
                                <tr>
                                    <td>${item.geneRefGene}</td>
                                    <td>${item.avsnp150}</td>
                                    <td>${item.chr}:${item.start}-${item.end}</td>
                                    <td>${item.ref} &gt; ${item.alt}</td>
                                    <td>
                                        <div>${item.funcRefGene}</div>
                                        <c:if test="${not empty item.exonicFuncRefGene and item.exonicFuncRefGene != '.'}">
                                            <div class="evidence-meta">Exonic: ${item.exonicFuncRefGene}</div>
                                        </c:if>
                                        <c:if test="${not empty item.aaChangeRefGene and item.aaChangeRefGene != '.'}">
                                            <div class="evidence-meta">AA: ${item.aaChangeRefGene}</div>
                                        </c:if>
                                    </td>
                                    <td>
                                        <span class="badge ${item.functionalStatus == 'Functional' ? 'badge-success' : (item.functionalStatus == 'Possibly functional' ? 'badge-warning' : 'badge-secondary')}">
                                            ${item.functionalStatus}
                                        </span>
                                    </td>
                                    <td>
                                        <span class="badge ${item.matched ? 'badge-success' : 'badge-secondary'}">
                                            ${item.matched ? 'Matched' : 'No match'}
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <h5 class="mt-4">B. Matched Pharmacogenetic Evidence</h5>
                    <c:if test="${empty matchedResults}">
                        <div class="alert alert-light border">No matched guideline result found.</div>
                    </c:if>
                    <c:if test="${not empty matchedResults}">
                        <div class="table-responsive">
                            <table class="table table-striped table-sm professional-table">
                                <thead>
                                <tr>
                                    <th>Matched Drug</th>
                                    <th>Gene</th>
                                    <th>Matching Level</th>
                                    <th>Guideline Source</th>
                                    <th>Guideline Summary Preview</th>
                                    <th>Evidence</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${matchedResults}" var="item">
                                    <tr>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty item.drugName}">
                                                    ${item.drugName} <span class="text-muted">(${item.drugId})</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-danger">Unresolved drug record (${item.drugId})</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${item.gene}</td>
                                        <td><span class="badge ${item.matchingBasis == 'Variant-level match' ? 'badge-success' : 'badge-warning'}">${item.matchingBasis}</span></td>
                                        <td>${item.source}</td>
                                        <td>
                                            <div class="guideline-summary-clamp" title="${item.guidelineSummary}">
                                                <c:choose>
                                                    <c:when test="${fn:length(item.guidelineSummary) > 220}">
                                                        ${fn:substring(item.guidelineSummary, 0, 220)}...
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${item.guidelineSummary}
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="evidence-meta mt-1">
                                                Recommendation:
                                                <span class="badge ${item.recommendationAvailable ? 'badge-success' : 'badge-secondary'}">
                                                    ${item.recommendationAvailable ? 'Available' : 'Not available'}
                                                </span>
                                            </div>
                                        </td>
                                        <td>
                                            <c:if test="${not empty item.drugId and not empty item.drugName}">
                                                <a class="btn btn-outline-primary btn-sm evidence-btn" href="<%=request.getContextPath()%>/drugDetail?id=${item.drugId}">
                                                    View Drug Evidence
                                                </a>
                                            </c:if>
                                            <c:if test="${empty item.drugName}">
                                                <span class="badge badge-danger">Unresolved</span>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:if>

                    <h5 class="mt-4">C. Source Evidence Availability</h5>
                    <c:if test="${empty sourceEvidenceSummaries}">
                        <div class="alert alert-light border">No source evidence summary available for this sample.</div>
                    </c:if>
                    <c:if test="${not empty sourceEvidenceSummaries}">
                        <div class="table-responsive">
                            <table class="table table-striped table-sm professional-table">
                                <thead>
                                <tr>
                                    <th>Drug</th>
                                    <th>Label Records</th>
                                    <th>Main Label Sources</th>
                                    <th>Guideline Records</th>
                                    <th>Evidence Page</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${sourceEvidenceSummaries}" var="item">
                                    <tr>
                                        <td>
                                            ${item.drugName}
                                            <c:if test="${not empty item.drugId}">
                                                <span class="text-muted">(${item.drugId})</span>
                                            </c:if>
                                        </td>
                                        <td>${item.labelRecordCount}</td>
                                        <td>${item.mainLabelSources}</td>
                                        <td>${item.guidelineRecordCount}</td>
                                        <td>
                                            <c:if test="${item.viewEvidenceEnabled}">
                                                <a class="btn btn-outline-primary btn-sm evidence-btn" href="<%=request.getContextPath()%>/drugDetail?id=${item.drugId}">
                                                    View Drug Evidence
                                                </a>
                                            </c:if>
                                            <c:if test="${not item.viewEvidenceEnabled}">
                                                <span class="badge badge-danger">Unresolved</span>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:if>
                </div>
                </c:if>

                <div class="mt-4 mb-4">
                    <h5>Disclaimer</h5>
                    <div class="disclaimer-box mb-2">
                        This system is for educational and research reference only. It does not provide diagnosis or medical advice.
                        Medication decisions should be made by qualified healthcare professionals.
                    </div>
                    <h6>Data Sources</h6>
                    <ul>
                        <li>PharmGKB-derived drug, drug label, and dosing guideline data imported in this system</li>
                        <li>User-uploaded ANNOVAR-style annotation files</li>
                    </ul>
                </div>
            </c:if>
        </main>
    </div>
</div>

<script>
    (function() {
        var canViewProfessional =
            <c:choose>
            <c:when test="${canViewProfessional}">true</c:when>
            <c:otherwise>false</c:otherwise>
            </c:choose>;
        if (!canViewProfessional) {
            return;
        }

        function setMode(mode) {
            var patient = document.getElementById('patientViewSection');
            var professional = document.getElementById('professionalViewSection');
            if (!patient || !professional) return;
            if (mode === 'patient') {
                patient.style.display = '';
                professional.style.display = 'none';
            } else if (mode === 'professional') {
                patient.style.display = 'none';
                professional.style.display = '';
            } else {
                patient.style.display = '';
                professional.style.display = '';
            }
        }

        var radios = document.querySelectorAll('input[name="viewMode"]');
        for (var i = 0; i < radios.length; i++) {
            radios[i].addEventListener('change', function(e) {
                setMode(e.target.value);
            });
        }
        setMode('patient');
    })();
</script>
</body>
</html>
