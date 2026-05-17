<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Data Sources and About</title>
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
            <jsp:param name="active" value="about" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>Data Sources and About</h2>
            </div>
            <p class="page-description">Reference notes for data provenance, module scope, and prototype constraints.</p>

            <div class="upload-main-card mb-3">
                <h5 class="mb-2">Data Sources</h5>
                <ul class="mb-0">
                    <li>Drug records: pharmacogenetic drug database entries imported into this platform.</li>
                    <li>Drug label evidence: regulatory label annotations linked by drug ID.</li>
                    <li>Dosing guideline evidence: guideline records (for example CPIC/DPWG style sources) linked by drug ID.</li>
                    <li>Mutation annotation input: user-uploaded ANNOVAR-style TSV/TXT files.</li>
                    <li>Variant-level references: `pgx_variant` records used for rsID-based variant-level matching.</li>
                </ul>
            </div>

            <div class="upload-main-card mb-3">
                <h5 class="mb-2">Prototype Scope</h5>
                <p class="mb-2">This prototype accepts ANNOVAR-style annotated files rather than raw VCF files.</p>
                <p class="mb-0">In a full deployment, raw VCF files may be annotated by external tools (such as ANNOVAR or VEP) before pharmacogenetic matching.</p>
            </div>

            <div class="disclaimer-box mb-4">
                Disclaimer: This system is for educational and research reference only. It does not provide diagnosis or medical advice. Medication decisions should be made by qualified healthcare professionals.
                <br>
                Privacy note: Uploaded files may contain sensitive genetic information. Do not upload real patient data in this prototype environment.
            </div>
        </main>
    </div>
</div>
</body>
</html>
