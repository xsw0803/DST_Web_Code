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
    <title>Drug Knowledge Base</title>

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
            <jsp:param name="active" value="drugs" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <div>
                    <h2>Drug Knowledge Base</h2>
                    <p class="text-muted mb-0">Search and review drugs in the pharmacogenomics knowledge base.</p>
                </div>
            </div>
            <form method="get" action="drugs" class="mb-3">
                <div class="input-group">
                    <input type="text" class="form-control" name="keyword"
                           value="${keyword}" placeholder="Search drug name">
                    <div class="input-group-append">
                        <button class="btn btn-primary" type="submit">Search</button>
                        <a class="btn btn-outline-secondary" href="drugs">Reset</a>
                    </div>
                </div>
            </form>
            <div class="d-flex justify-content-between align-items-center mb-2">
                <small class="text-muted">
                    Showing ${drugs.size()} result(s)
                </small>
            </div>
            <div class="table-responsive">
                <c:choose>
                    <c:when test="${!drugs.isEmpty()}">
                        <table class="table table-striped table-sm">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Name</th>
                                <th>Drug Url</th>
                                <th>Biomarker</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${drugs}" var="item">
                                <tr>
                                    <td>${item.id}</td>
                                    <td><a href="drugDetail?id=${item.id}">${item.name}</a></td>
                                    <td>${item.drugUrl}</td>
                                    <td>${item.biomarker}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning" role="alert">
                            No drugs were found for the current search condition.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>
</div>
</body>
</html>
