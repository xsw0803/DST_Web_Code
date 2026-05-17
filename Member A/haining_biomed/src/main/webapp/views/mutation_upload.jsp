<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Mutation Upload and Matching</title>
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
                <h2>Mutation Upload and Matching</h2>
            </div>
            <p class="page-description">Upload an ANNOVAR-style annotation file to identify pharmacogenetic variants and match them with drug labels and dosing guidelines.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>

            <div class="row">
                <div class="col-lg-8 mb-4">
                    <div class="upload-main-card">
                        <h4 class="mb-3">Upload ANNOVAR-style Annotation File</h4>

                        <div class="upload-section-title">1. Required columns</div>
                        <p class="upload-muted mb-2">The uploaded file should be a tab-separated ANNOVAR-style annotation table with these columns:</p>
                        <div class="upload-badge-group mb-3">
                            <span class="badge badge-secondary">Chr</span>
                            <span class="badge badge-secondary">Start</span>
                            <span class="badge badge-secondary">End</span>
                            <span class="badge badge-secondary">Ref</span>
                            <span class="badge badge-secondary">Alt</span>
                            <span class="badge badge-secondary">Func.refGene</span>
                            <span class="badge badge-secondary">Gene.refGene</span>
                            <span class="badge badge-secondary">ExonicFunc.refGene</span>
                            <span class="badge badge-secondary">AAChange.refGene</span>
                            <span class="badge badge-secondary">avsnp150</span>
                        </div>

                        <div class="upload-section-title">2. Demo files for testing</div>
                        <p class="upload-muted mb-2">Recommended: use the mixed-case demo to test variant-level matching, gene-level fallback, and no-match handling in one upload.</p>
                        <div class="mb-3">
                            <a class="btn btn-sm btn-outline-primary mr-2 mb-2" href="<%=request.getContextPath()%>/static/demo/demo_mixed_case.tsv">Download mixed-case demo</a>
                            <a class="btn btn-sm btn-outline-primary mr-2 mb-2" href="<%=request.getContextPath()%>/static/demo/demo_variant_level.tsv">Download variant-level demo</a>
                            <a class="btn btn-sm btn-outline-primary mr-2 mb-2" href="<%=request.getContextPath()%>/static/demo/demo_gene_fallback.tsv">Download gene-fallback demo</a>
                            <a class="btn btn-sm btn-outline-primary mr-2 mb-2" href="<%=request.getContextPath()%>/static/demo/demo_no_match.tsv">Download no-match demo</a>
                            <a class="btn btn-sm btn-outline-primary mr-2 mb-2" href="<%=request.getContextPath()%>/static/demo/demo_annovar.tsv">Download basic ANNOVAR demo</a>
                        </div>

                        <div class="upload-section-title">3. Choose file and run matching</div>
                        <form method="post" action="<%=request.getContextPath()%>/mutationUpload" enctype="multipart/form-data">
                            <label class="upload-label" for="annovarFileInput">Choose annotation file</label>
                            <div class="upload-file-wrap mb-3">
                                <input id="annovarFileInput" type="file" class="form-control-file" name="annovarFile" accept=".txt,.tsv">
                            </div>
                            <button type="submit" class="btn btn-primary">Upload and Run Matching</button>
                        </form>
                    </div>
                </div>

                <div class="col-lg-4 mb-4">
                    <div class="upload-side-card">
                        <h5 class="mb-2">What happens after upload?</h5>
                        <ol class="upload-steps mb-3">
                            <li>Parse annotation rows.</li>
                            <li>Filter potentially functional variants.</li>
                            <li>Run variant-level matching when rsID evidence is available.</li>
                            <li>Apply gene-level fallback when variant-level evidence is unavailable.</li>
                            <li>Generate patient and professional interpretation.</li>
                        </ol>
                        <div class="upload-check-title">Before uploading, please ensure:</div>
                        <ul class="upload-check-list mb-0">
                            <li>The file is tab-separated.</li>
                            <li>The first row contains column names.</li>
                            <li><code>Gene.refGene</code> and <code>avsnp150</code> are included.</li>
                            <li>Each row represents one annotated variant.</li>
                        </ul>
                    </div>
                </div>
            </div>

            <div class="mt-4 disclaimer-box">
                Disclaimer: This system is for educational and research reference only. It does not provide diagnosis or medical advice.
                Medication decisions should be made by qualified healthcare professionals.
                <br>
                Privacy note: Uploaded files may contain sensitive genetic information. Do not upload real patient data in this prototype environment.
            </div>
        </main>
    </div>
</div>
</body>
</html>
