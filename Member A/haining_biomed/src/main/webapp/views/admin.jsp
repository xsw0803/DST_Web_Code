<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Admin Dashboard</title>
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
            <jsp:param name="active" value="admin" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>Admin Dashboard</h2>
            </div>
            <p class="page-description">Administrative center for monitoring uploaded samples, maintaining PGx variant mappings, and reviewing pharmacogenetic knowledge-base records.</p>

            <h4>1. System Overview</h4>
            <p class="upload-muted">Current database status and uploaded sample monitoring.</p>
            <div class="summary-panel mb-4">
                <div class="row">
                    <div class="col-md-4 col-lg-2 mb-2">
                        <div class="summary-metric-card">
                            <div class="metric-label">Drug Records</div>
                            <div class="metric-value">${drugCount}</div>
                        </div>
                    </div>
                    <div class="col-md-4 col-lg-2 mb-2">
                        <div class="summary-metric-card">
                            <div class="metric-label">Drug Labels</div>
                            <div class="metric-value">${drugLabelCount}</div>
                        </div>
                    </div>
                    <div class="col-md-4 col-lg-2 mb-2">
                        <div class="summary-metric-card">
                            <div class="metric-label">Dosing Guidelines</div>
                            <div class="metric-value">${guidelineCount}</div>
                        </div>
                    </div>
                    <div class="col-md-4 col-lg-2 mb-2">
                        <div class="summary-metric-card">
                            <div class="metric-label">PGx Variants</div>
                            <div class="metric-value">${pgxVariantCount}</div>
                        </div>
                    </div>
                    <div class="col-md-4 col-lg-2 mb-2">
                        <div class="summary-metric-card">
                            <div class="metric-label">Uploaded Samples</div>
                            <div class="metric-value">${uploadedSampleCount}</div>
                        </div>
                    </div>
                </div>
            </div>

            <h4>2. Management Shortcuts</h4>
            <p class="upload-muted">Use these entries to review core records and monitor matching-related data.</p>
            <div class="row mb-4">
                <div class="col-md-6 col-xl-4 mb-3">
                    <a class="quick-action-card" href="<%=request.getContextPath()%>/drugs">
                        <div class="action-title">Manage Drug Records</div>
                        <div class="action-desc">Review drug-level records, biomarker status, and PharmGKB links.</div>
                    </a>
                </div>
                <div class="col-md-6 col-xl-4 mb-3">
                    <a class="quick-action-card" href="<%=request.getContextPath()%>/drugLabels">
                        <div class="action-title">Manage Drug Labels</div>
                        <div class="action-desc">Inspect label sources, dosing information flags, and summary quality.</div>
                    </a>
                </div>
                <div class="col-md-6 col-xl-4 mb-3">
                    <a class="quick-action-card" href="<%=request.getContextPath()%>/dosingGuideline">
                        <div class="action-title">Manage Dosing Guidelines</div>
                        <div class="action-desc">Review recommendation availability, sources, and guideline evidence.</div>
                    </a>
                </div>
                <div class="col-md-6 col-xl-4 mb-3">
                    <a class="quick-action-card" href="<%=request.getContextPath()%>/sampleHistory">
                        <div class="action-title">Review Uploaded Samples</div>
                        <div class="action-desc">Track sample upload history and open matching results by sample.</div>
                    </a>
                </div>
                <div class="col-md-6 col-xl-4 mb-3">
                    <a class="quick-action-card" href="<%=request.getContextPath()%>/mutationSearch">
                        <div class="action-title">Search Annotation Records</div>
                        <div class="action-desc">Query uploaded mutation annotations by sample, gene, rsID, or location.</div>
                    </a>
                </div>
                <div class="col-md-6 col-xl-4 mb-3">
                    <a class="quick-action-card" href="<%=request.getContextPath()%>/pgxVariants">
                        <div class="action-title">PGx Variant Record Management</div>
                        <div class="action-desc">Maintain rsID-to-gene mappings for variant-level matching and evidence traceability.</div>
                    </a>
                </div>
            </div>

            <h4>3. Data Quality Warnings</h4>
            <div class="alert alert-light border mb-4">
                <div>Guideline records referencing missing drug IDs: <strong>${unresolvedGuidelineDrugCount}</strong></div>
                <div>PGx variant records referencing missing drug IDs: <strong>${unresolvedPgxDrugCount}</strong></div>
            </div>

            <h4>4. Data Maintenance Notes</h4>
            <div class="disclaimer-box mb-4">
                <ul class="mb-0">
                    <li>Keep identifiers consistent across `drug`, `drug_label`, `dosing_guideline`, and `pgx_variant` tables.</li>
                    <li>Review unresolved drug records detected in mutation matching results.</li>
                    <li>Maintain rsID mappings to support variant-level matching and transparent fallback behavior.</li>
                    <li>Preserve source metadata for traceable pharmacogenetic interpretation.</li>
                </ul>
            </div>

            <h4>5. Admin Notice</h4>
            <div class="alert alert-light border mb-4">
                Administrative actions should be restricted to authorized users and reviewed before data-level changes are applied.
            </div>
        </main>
    </div>
</div>
</body>
</html>
