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
            <jsp:param name="active" value="drugs" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>Drugs</h2>
            </div>
            <p class="page-description">Browse pharmacogenetic drug records, check biomarker availability, and open detailed drug-label and dosing-guideline evidence.</p>
            <form class="form-inline mb-3" method="get" action="<%=request.getContextPath()%>/drugs">
                <input class="form-control mr-sm-2 drug-search-input" type="search" name="q" placeholder="Search by drug name or ID" value="${q}">
                <button class="btn btn-primary my-2 my-sm-0 mr-sm-2" type="submit">Search</button>
                <select class="form-control mr-sm-2" name="biomarker">
                    <option value="all" ${biomarker == 'all' ? 'selected' : ''}>Biomarker: All</option>
                    <option value="yes" ${biomarker == 'yes' ? 'selected' : ''}>Biomarker: Available</option>
                    <option value="no" ${biomarker == 'no' ? 'selected' : ''}>Biomarker: Not available</option>
                </select>
                <a class="btn btn-outline-secondary my-2 my-sm-0" href="<%=request.getContextPath()%>/drugs">Reset</a>
            </form>

            <div class="summary-panel mb-3 drug-result-summary">
                <c:choose>
                    <c:when test="${empty q and biomarker == 'all'}">
                        Showing ${resultCount} drug records
                    </c:when>
                    <c:when test="${not empty q and biomarker == 'all'}">
                        Showing ${resultCount} results for "${q}"
                    </c:when>
                    <c:when test="${empty q and biomarker == 'yes'}">
                        Showing ${resultCount} drugs with available biomarkers
                    </c:when>
                    <c:when test="${empty q and biomarker == 'no'}">
                        Showing ${resultCount} drugs without available biomarkers
                    </c:when>
                    <c:when test="${not empty q and biomarker == 'yes'}">
                        Showing ${resultCount} results for "${q}" with available biomarkers
                    </c:when>
                    <c:otherwise>
                        Showing ${resultCount} results for "${q}" without available biomarkers
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="alert alert-light border mb-3 drug-tip">
                Tip: Drugs marked as “Available” may have linked pharmacogenetic labels or dosing guidelines. Open “View Evidence” for details.
            </div>

            <c:if test="${empty drugs}">
                <div class="alert alert-light border">
                    No pharmacogenetic drug records matched your search term. Try another drug name or Drug ID.
                </div>
            </c:if>

            <c:if test="${not empty drugs}">
            <div class="table-responsive">
                <table class="table table-striped table-sm drug-table">
                    <colgroup>
                        <col style="width:42%">
                        <col style="width:14%">
                        <col style="width:16%">
                        <col style="width:14%">
                        <col style="width:14%">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>Drug Name</th>
                        <th>Drug ID</th>
                        <th>Biomarker Available</th>
                        <th>PharmGKB</th>
                        <th>Evidence</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${drugs}" var="item">
                        <tr>
                            <td><span class="drug-name">${item.name}</span></td>
                            <td><span class="drug-id">${item.id}</span></td>
                            <td>
                                <span class="badge ${item.biomarker ? 'badge-success' : 'badge-secondary'}">
                                    ${item.biomarker ? 'Available' : 'Not available'}
                                </span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty item.drugUrl}">
                                        <a href="https://www.pharmgkb.org${item.drugUrl}" target="_blank">Open</a>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">Not available</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><a class="btn btn-sm btn-outline-primary evidence-btn" href="<%=request.getContextPath()%>/drugDetail?id=${item.id}">View Evidence</a></td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>
            </div>
            </c:if>
            <div class="mt-4 disclaimer-box">
                Data sources are preserved for pharmacogenetic reference. This page does not provide direct medical advice.
            </div>
        </main>
    </div>
</div>
</body>
</html>
