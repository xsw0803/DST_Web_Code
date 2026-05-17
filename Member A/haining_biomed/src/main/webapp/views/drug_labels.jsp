<%--
  Created by IntelliJ IDEA.
  User: hello
  Date: 2019-12-3
  Time: 15:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
            <jsp:param name="active" value="drug_labels" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>Drug Labels</h2>
            </div>
            <p class="page-description">Review drug label evidence sources and dosing-related label summaries.</p>
            <form class="form-inline mb-3" method="get" action="<%=request.getContextPath()%>/drugLabels">
                <input class="form-control mr-sm-2 drug-search-input" type="search" name="q" placeholder="Search by drug, source, or label keyword" value="${q}">
                <button class="btn btn-primary my-2 my-sm-0 mr-sm-2" type="submit">Search</button>
                <select class="form-control mr-sm-2" name="dosing">
                    <option value="all" ${dosing == 'all' ? 'selected' : ''}>Dosing info: All</option>
                    <option value="yes" ${dosing == 'yes' ? 'selected' : ''}>Dosing info: Available</option>
                    <option value="no" ${dosing == 'no' ? 'selected' : ''}>Dosing info: Not available</option>
                </select>
                <a class="btn btn-outline-secondary my-2 my-sm-0" href="<%=request.getContextPath()%>/drugLabels">Reset</a>
            </form>

            <div class="summary-panel mb-3 drug-result-summary">
                <c:choose>
                    <c:when test="${empty q and dosing == 'all'}">
                        Showing ${resultCount} drug label records
                    </c:when>
                    <c:when test="${not empty q and dosing == 'all'}">
                        Showing ${resultCount} results for "${q}"
                    </c:when>
                    <c:when test="${empty q and dosing == 'yes'}">
                        Showing ${resultCount} records with dosing information
                    </c:when>
                    <c:when test="${empty q and dosing == 'no'}">
                        Showing ${resultCount} records without dosing information
                    </c:when>
                    <c:when test="${not empty q and dosing == 'yes'}">
                        Showing ${resultCount} results for "${q}" with dosing information
                    </c:when>
                    <c:otherwise>
                        Showing ${resultCount} results for "${q}" without dosing information
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="alert alert-light border mb-3 drug-tip">
                Tip: Records marked as “Available” include dosing-related label evidence. Open “View Evidence” for integrated interpretation.
            </div>

            <c:if test="${empty drugLabels}">
                <div class="alert alert-light border">
                    No drug label records matched your search criteria. Try another label ID, source, or Drug ID.
                </div>
            </c:if>

            <c:if test="${not empty drugLabels}">
                <div class="table-responsive">
                    <table class="table table-striped table-sm label-table">
                        <colgroup>
                            <col style="width:18%">
                            <col style="width:10%">
                            <col style="width:14%">
                            <col style="width:46%">
                            <col style="width:12%">
                        </colgroup>
                        <thead>
                        <tr>
                            <th>Drug</th>
                            <th>Source</th>
                            <th>Dosing Information</th>
                            <th>Label Summary</th>
                            <th>Evidence</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${drugLabels}" var="item">
                            <tr>
                                <td>
                                    <div class="drug-name">${empty item.drugName ? 'Unknown Drug' : item.drugName}</div>
                                    <div class="drug-id">Label ID: ${item.id}</div>
                                </td>
                                <td>
                                    <c:set var="lowerSource" value="${fn:toLowerCase(item.source)}" />
                                    <c:choose>
                                        <c:when test="${fn:contains(lowerSource, 'u.s. food and drug administration')}">FDA</c:when>
                                        <c:when test="${fn:contains(lowerSource, 'european medicines agency')}">EMA</c:when>
                                        <c:when test="${fn:contains(lowerSource, 'health canada')}">Health Canada</c:when>
                                        <c:when test="${fn:contains(lowerSource, 'pharmaceuticals and medical devices agency')}">PMDA Japan</c:when>
                                        <c:otherwise>${item.source}</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <span class="badge ${item.dosingInformation ? 'badge-success' : 'badge-secondary'}">
                                        ${item.dosingInformation ? 'Available' : 'Not available'}
                                    </span>
                                </td>
                                <td title="${item.summaryMarkdown}">
                                    <span class="label-summary-clamp">${empty item.summaryMarkdown ? 'No summary available.' : item.summaryMarkdown}</span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty item.drugId}">
                                            <a class="btn btn-sm btn-outline-primary evidence-btn" href="<%=request.getContextPath()%>/drugDetail?id=${item.drugId}">View Evidence</a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">Not linked</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
            <div class="mt-4 disclaimer-box">
                Drug label summaries are shown for educational and reference use. Professional interpretation is recommended.
            </div>
        </main>
    </div>
</div>
</body>
</html>
