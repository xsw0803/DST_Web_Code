<%--
  Created by IntelliJ IDEA.
  User: hello
  Date: 2019-12-3
  Time: 15:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="generator" content="">
    <title>Precision Medicine Matching System</title>

    <!-- Bootstrap core CSS -->
    <link href="<%=request.getContextPath()%>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="<%=request.getContextPath()%>/static/jquery/jquery-3.4.1.js"></script>
    <script src="<%=request.getContextPath()%>/static/bootstrap/js/bootstrap.bundle.min.js"></script>
    <!-- Custom styles for this template -->
    <link href="<%=request.getContextPath()%>/static/css/app.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/static/css/style.css" rel="stylesheet">
    <style>
        .bd-placeholder-img {
            font-size: 1.125rem;
            text-anchor: middle;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }

        @media (min-width: 768px) {
            .bd-placeholder-img-lg {
                font-size: 3.5rem;
            }
        }
    </style>
</head>
<body>
<jsp:include page="head.jsp" />

<div class="container-fluid">
    <div class="row">
        <jsp:include page="nav.jsp" >
            <jsp:param name="active" value="dashboard" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>Dashboard</h2>
            </div>
            <p class="page-description">A pharmacogenetic knowledge platform for drug evidence browsing, ANNOVAR-style mutation upload, and variant-based matching.</p>

            <div class="hero-panel mb-4">
                <h4>Precision Medicine Matching System</h4>
                <p class="mb-2">
                    This system integrates drug records, drug label annotations, dosing guidelines, and mutation annotation data
                    to support pharmacogenetic evidence browsing and mutation-based drug matching.
                </p>
                <div class="mb-0">Use this system to:</div>
                <ul class="mb-0">
                    <li>Browse pharmacogenetic drug records</li>
                    <li>Review drug labels and dosing guidelines</li>
                    <li>Upload ANNOVAR-style mutation annotation files</li>
                    <li>Match variants with drug-related pharmacogenetic evidence</li>
                </ul>
            </div>

            <h4 class="mb-3">Quick Actions</h4>
            <div class="row mb-4">
                <div class="col-md-6 col-xl-3 mb-3">
                    <a class="quick-action-card" href="<%=request.getContextPath()%>/drugs">
                        <div class="action-title">Browse Drugs</div>
                        <div class="action-desc">Browse drug records and biomarker availability.</div>
                    </a>
                </div>
                <div class="col-md-6 col-xl-3 mb-3">
                    <a class="quick-action-card" href="<%=request.getContextPath()%>/drugLabels">
                        <div class="action-title">Drug Labels</div>
                        <div class="action-desc">Review label source evidence and dosing information.</div>
                    </a>
                </div>
                <div class="col-md-6 col-xl-3 mb-3">
                    <a class="quick-action-card" href="<%=request.getContextPath()%>/dosingGuideline">
                        <div class="action-title">Dosing Guidelines</div>
                        <div class="action-desc">Search pharmacogenetic dosing recommendations.</div>
                    </a>
                </div>
                <div class="col-md-6 col-xl-3 mb-3">
                    <a class="quick-action-card" href="<%=request.getContextPath()%>/mutationUpload">
                        <div class="action-title">Mutation Upload</div>
                        <div class="action-desc">Upload ANNOVAR-style annotation files for matching.</div>
                    </a>
                </div>
                <c:if test="${sessionScope.role == 'admin'}">
                    <div class="col-md-6 col-xl-3 mb-3">
                        <a class="quick-action-card" href="<%=request.getContextPath()%>/sampleHistory">
                            <div class="action-title">Sample History</div>
                            <div class="action-desc">Review previous uploaded samples and matching results.</div>
                        </a>
                    </div>
                </c:if>
            </div>

            <h4 class="mb-3">Recommended Workflow</h4>
            <div class="row mb-4">
                <div class="col-md-6 col-xl-4 mb-3">
                    <div class="workflow-card">
                        <div class="workflow-step-number">1</div>
                        <div><strong>Browse Knowledge Base</strong></div>
                        <div class="action-desc">Inspect drug, label, and guideline records.</div>
                    </div>
                </div>
                <div class="col-md-6 col-xl-4 mb-3">
                    <div class="workflow-card">
                        <div class="workflow-step-number">2</div>
                        <div><strong>Upload Annotation File</strong></div>
                        <div class="action-desc">Upload ANNOVAR-style mutation annotation data.</div>
                    </div>
                </div>
                <div class="col-md-6 col-xl-4 mb-3">
                    <div class="workflow-card">
                        <div class="workflow-step-number">3</div>
                        <div><strong>Variant Filtering</strong></div>
                        <div class="action-desc">System filters potentially functional variants.</div>
                    </div>
                </div>
                <div class="col-md-6 col-xl-4 mb-3">
                    <div class="workflow-card">
                        <div class="workflow-step-number">4</div>
                        <div><strong>Matching</strong></div>
                        <div class="action-desc">Variant-level match first, then gene-level fallback.</div>
                    </div>
                </div>
                <div class="col-md-6 col-xl-4 mb-3">
                    <div class="workflow-card">
                        <div class="workflow-step-number">5</div>
                        <div><strong>Interpretation</strong></div>
                        <div class="action-desc">View simplified and professional result layers.</div>
                    </div>
                </div>
            </div>

            <h4 class="mb-3">Current Role</h4>
            <div class="role-card mb-4">
                <div class="mb-2">
                    Logged in as:
                    <strong>${empty sessionScope.display_name ? 'Guest' : sessionScope.display_name}</strong>
                    <c:choose>
                        <c:when test="${sessionScope.role == 'admin'}">
                            <span class="badge badge-success">Admin</span>
                        </c:when>
                        <c:when test="${sessionScope.role == 'professional'}">
                            <span class="badge badge-success">Professional</span>
                        </c:when>
                        <c:when test="${sessionScope.role == 'patient'}">
                            <span class="badge badge-secondary">Patient</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge badge-secondary">Guest</span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="mb-1"><strong>Available permissions:</strong></div>
                <ul class="mb-0">
                    <c:choose>
                        <c:when test="${sessionScope.role == 'admin'}">
                            <li>Browse drug knowledge base</li>
                            <li>Upload mutation annotation files</li>
                            <li>View professional matching details</li>
                            <li>Access sample history</li>
                            <li>Access admin management entry</li>
                        </c:when>
                        <c:when test="${sessionScope.role == 'professional'}">
                            <li>Browse drugs, labels, and dosing guidelines</li>
                            <li>Upload mutation annotation files</li>
                            <li>View simplified and professional evidence details</li>
                        </c:when>
                        <c:when test="${sessionScope.role == 'patient'}">
                            <li>Browse pharmacogenetic knowledge base</li>
                            <li>Upload mutation annotation files</li>
                            <li>View simplified interpretation results</li>
                        </c:when>
                        <c:otherwise>
                            <li>Browse public knowledge base pages</li>
                            <li>Sign in to use upload and matching modules</li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>

            <h4 class="mb-3">Data Sources and Disclaimer</h4>
            <div class="disclaimer-box">
                <div><strong>Data Sources:</strong> pharmacogenetic drug records, drug label annotations, dosing guideline evidence, and user-uploaded ANNOVAR-style mutation annotation files.</div>
                <div class="mt-2"><strong>Disclaimer:</strong> This system is for educational and research reference only. It does not provide diagnosis or medical advice. Medication decisions should be made by qualified healthcare professionals.</div>
            </div>
        </main>
    </div>
</div>
</body>
</html>
