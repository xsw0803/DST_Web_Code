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
            <jsp:param name="active" value="dosing_guideline" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>Dosing Guidelines</h2>
            </div>
            <p class="page-description">Browse pharmacogenetic dosing guidance and recommendation evidence by drug and source.</p>
            <form class="form-inline mb-3" method="get" action="<%=request.getContextPath()%>/dosingGuideline">
                <input class="form-control mr-sm-2 drug-search-input" type="search" name="q" placeholder="Search by guideline, drug, gene, or source" value="${q}">
                <button class="btn btn-primary my-2 my-sm-0 mr-sm-2" type="submit">Search</button>
                <select class="form-control mr-sm-2" name="recommendation">
                    <option value="all" ${recommendation == 'all' ? 'selected' : ''}>Recommendation: All</option>
                    <option value="yes" ${recommendation == 'yes' ? 'selected' : ''}>Recommendation: Available</option>
                    <option value="no" ${recommendation == 'no' ? 'selected' : ''}>Recommendation: Not available</option>
                </select>
                <a class="btn btn-outline-secondary my-2 my-sm-0" href="<%=request.getContextPath()%>/dosingGuideline">Reset</a>
            </form>

            <div class="summary-panel mb-3 drug-result-summary">
                <c:choose>
                    <c:when test="${empty q and recommendation == 'all'}">
                        Showing ${resultCount} dosing guideline records
                    </c:when>
                    <c:when test="${not empty q and recommendation == 'all'}">
                        Showing ${resultCount} results for "${q}"
                    </c:when>
                    <c:when test="${empty q and recommendation == 'yes'}">
                        Showing ${resultCount} records with available recommendations
                    </c:when>
                    <c:when test="${empty q and recommendation == 'no'}">
                        Showing ${resultCount} records without specific recommendations
                    </c:when>
                    <c:when test="${not empty q and recommendation == 'yes'}">
                        Showing ${resultCount} results for "${q}" with available recommendations
                    </c:when>
                    <c:otherwise>
                        Showing ${resultCount} results for "${q}" without specific recommendations
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="alert alert-light border mb-3 drug-tip">
                Tip: Recommendation status indicates whether actionable dosing guidance is available. Open “View Evidence” for full source-specific interpretation.
            </div>

            <c:if test="${empty dosingGuidelines}">
                <div class="alert alert-light border">
                    No dosing guideline records matched your search criteria. Try another guideline name, source, or Drug ID.
                </div>
            </c:if>

            <c:if test="${not empty dosingGuidelines}">
                <div class="table-responsive">
                    <table class="table table-striped table-sm guideline-table">
                        <colgroup>
                            <col style="width:28%">
                            <col style="width:16%">
                            <col style="width:14%">
                            <col style="width:10%">
                            <col style="width:20%">
                            <col style="width:12%">
                        </colgroup>
                        <thead>
                        <tr>
                            <th>Guideline</th>
                            <th>Drug</th>
                            <th>Recommendation</th>
                            <th>Source</th>
                            <th>Summary Preview</th>
                            <th>Evidence</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${dosingGuidelines}" var="item">
                            <tr>
                                <td>
                                    <div class="drug-name">${item.name}</div>
                                    <div class="drug-id">Guideline ID: ${item.id}</div>
                                </td>
                                <td>
                                    <div class="drug-name">${empty item.drugName ? 'Unknown Drug' : item.drugName}</div>
                                    <div class="drug-id">${item.drugId}</div>
                                </td>
                                <td>
                                    <span class="badge ${item.recommendation ? 'badge-success' : 'badge-secondary'}">
                                        ${item.recommendation ? 'Available' : 'Not available'}
                                    </span>
                                </td>
                                <td>
                                    <c:set var="lowerSource" value="${fn:toLowerCase(item.source)}" />
                                    <c:choose>
                                        <c:when test="${fn:contains(lowerSource, 'clinical pharmacogenetics implementation consortium')}">CPIC</c:when>
                                        <c:when test="${fn:contains(lowerSource, 'dutch pharmacogenetics working group')}">DPWG</c:when>
                                        <c:when test="${fn:contains(lowerSource, 'canadian pharmacogenomics network')}">CPNDS</c:when>
                                        <c:when test="${fn:contains(lowerSource, 'fda')}">FDA</c:when>
                                        <c:otherwise>${item.source}</c:otherwise>
                                    </c:choose>
                                </td>
                                <td title="${item.summaryMarkdown}">
                                    <c:set var="cleanSummary" value="${fn:replace(fn:replace(fn:replace(item.summaryMarkdown, '<p>', ''), '</p>', ''), '<br>', ' ')}" />
                                    <span class="guideline-summary-clamp">${empty cleanSummary ? 'No summary available.' : cleanSummary}</span>
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
                Guideline evidence shown here supports pharmacogenetic reference and does not replace clinical judgment.
            </div>
        </main>
    </div>
</div>
</body>
</html>
