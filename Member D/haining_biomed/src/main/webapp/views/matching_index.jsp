<%--
  Created by IntelliJ IDEA.
  User: hello
  Date: 2019-12-3
  Time: 15:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="generator" content="">
    <title>Patient Variant Matching</title>
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
            <jsp:param name="active" value="matching_index" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <div>
                    <h2>Patient Variant Matching</h2>
                    <p class="text-muted mb-0">Upload an Annovar output file to match patient variants with pharmacogenomics drug labels.</p>
                </div>
            </div>

            <div class="alert alert-info" role="alert">
                <strong>Workflow:</strong>
                upload variant annotation file, save sample information, identify non-synonymous genes, and return matched drug labels.
            </div>

            <div class="row">
                <div class="col-lg-7">
                    <div class="card shadow-sm mb-4">
                        <div class="card-header bg-primary text-white">
                            Upload Annovar Output
                        </div>
                        <div class="card-body">
                            <form method="post" action="upload" enctype="multipart/form-data">
                                <div class="form-group">
                                    <label for="uploaded_by">Uploaded By</label>
                                    <input type="text" class="form-control" id="uploaded_by" name="uploaded_by"
                                           placeholder="Enter your name or team member ID">
                                    <small class="form-text text-muted">This name will be stored with the sample record.</small>
                                </div>

                                <div class="form-group">
                                    <label for="annovarFile">Annovar Output File</label>
                                    <input type="file" class="form-control-file" id="annovarFile" name="annovar">
                                    <small class="form-text text-muted">Please upload a tab-delimited Annovar annotation result.</small>
                                </div>

                                <button type="submit" class="btn btn-primary">
                                    Upload and Match
                                </button>
                                <a href="samples" class="btn btn-outline-secondary ml-2">
                                    View Sample History
                                </a>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="col-lg-5">
                    <div class="card shadow-sm mb-4">
                        <div class="card-header">
                            Matching Output
                        </div>
                        <div class="card-body">
                            <p class="mb-2">After successful upload, the system will generate:</p>
                            <ul class="mb-0">
                                <li>A new sample record</li>
                                <li>A list of detected variant genes</li>
                                <li>Matched pharmacogenomics drug labels</li>
                                <li>Evidence summary from the knowledge base</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>
