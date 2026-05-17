<%--
  Created by IntelliJ IDEA.
  User: hello
  Date: 2019-12-3
  Time: 15:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="generator" content="">
    <title>Precision Medicine Dashboard</title>

    <!-- Bootstrap core CSS -->
    <link href="<%=request.getContextPath()%>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="<%=request.getContextPath()%>/static/jquery/jquery-3.4.1.js"></script>
    <script src="<%=request.getContextPath()%>/static/bootstrap/js/bootstrap.bundle.min.js"></script>
    <!-- Custom styles for this template -->
    <link href="<%=request.getContextPath()%>/static/css/app.css" rel="stylesheet">
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
<nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
    <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">Precision Medicine Matching System</a>

</nav>

<div class="container-fluid">
    <div class="row">
        <jsp:include page="nav.jsp" >
            <jsp:param name="active" value="dashboard" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <div>
                    <h2>Precision Medicine Dashboard</h2>
                    <p class="text-muted mb-0">Access matching workflow, sample history, and pharmacogenomics knowledge bases.</p>
                </div>
            </div>
            <div class="row mb-4">
                <div class="col-lg-12">
                    <div class="alert alert-info" role="alert">
                        <h4 class="alert-heading">Precision Medicine Matching System</h4>
                        <p class="mb-0">
                            This system supports Annovar output upload, pharmacogenomics matching,
                            sample history review, and knowledge base browsing.
                        </p>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 col-lg-4 mb-4">
                    <div class="card shadow-sm h-100">
                        <div class="card-body">
                            <h5 class="card-title">Patient Variant Matching</h5>
                            <p class="card-text">Upload an Annovar output file and match patient variants with drug labels.</p>
                            <a href="matchingIndex" class="btn btn-primary btn-sm">Start Matching</a>
                        </div>
                    </div>
                </div>

                <div class="col-md-6 col-lg-4 mb-4">
                    <div class="card shadow-sm h-100">
                        <div class="card-body">
                            <h5 class="card-title">Sample History</h5>
                            <p class="card-text">Review uploaded samples and reopen previous matching results.</p>
                            <a href="samples" class="btn btn-outline-primary btn-sm">View Samples</a>
                        </div>
                    </div>
                </div>

                <div class="col-md-6 col-lg-4 mb-4">
                    <div class="card shadow-sm h-100">
                        <div class="card-body">
                            <h5 class="card-title">Drugs</h5>
                            <p class="card-text">Browse and search drug records in the pharmacogenomics knowledge base.</p>
                            <a href="drugs" class="btn btn-outline-secondary btn-sm">Open Drugs</a>
                        </div>
                    </div>
                </div>

                <div class="col-md-6 col-lg-4 mb-4">
                    <div class="card shadow-sm h-100">
                        <div class="card-body">
                            <h5 class="card-title">Drug Labels</h5>
                            <p class="card-text">Search clinical drug label annotations by name, source, or summary.</p>
                            <a href="drugLabels" class="btn btn-outline-secondary btn-sm">Open Drug Labels</a>
                        </div>
                    </div>
                </div>

                <div class="col-md-6 col-lg-4 mb-4">
                    <div class="card shadow-sm h-100">
                        <div class="card-body">
                            <h5 class="card-title">Dosing Guidelines</h5>
                            <p class="card-text">Search dosing recommendations by drug id, source, or summary.</p>
                            <a href="dosingGuideline" class="btn btn-outline-secondary btn-sm">Open Guidelines</a>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>
