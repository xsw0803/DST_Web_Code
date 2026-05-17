<%--
  Created by IntelliJ IDEA.
  User: hello
  Date: 2019-12-3
  Time: 15:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="generator" content="">
    <title>Matching Upload Error</title>

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
                    <h2>Upload Validation Failed</h2>
                    <p class="text-muted mb-0">The sample was not saved because the submitted Annovar file did not pass validation.</p>
                </div>
            </div>

            <div class="alert alert-danger" role="alert">
                <h4 class="alert-heading">Please check your upload</h4>
                <c:if test="${validateError != null}">
                    <p class="mb-0"><c:out value="${validateError}"></c:out></p>
                </c:if>
            </div>

            <div class="card shadow-sm mb-4">
                <div class="card-header">
                    Required Input
                </div>
                <div class="card-body">
                    <ul class="mb-3">
                        <li>Uploaded By must not be empty.</li>
                        <li>The Annovar output file must not be empty.</li>
                        <li>The file must be a tab-delimited Annovar annotation result.</li>
                        <li>Each data row should contain enough annotation columns for the matching workflow.</li>
                    </ul>
                    <a href="matchingIndex" class="btn btn-primary">Back to Upload</a>
                    <a href="samples" class="btn btn-outline-secondary ml-2">View Sample History</a>
                </div>
            </div>
        </main>

    </div>
</div>
</body>
</html>
