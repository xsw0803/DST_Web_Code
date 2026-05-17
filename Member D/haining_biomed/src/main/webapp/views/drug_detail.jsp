<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Drug Detail</title>
    <link href="<%=request.getContextPath()%>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="<%=request.getContextPath()%>/static/jquery/jquery-3.4.1.js"></script>
    <script src="<%=request.getContextPath()%>/static/bootstrap/js/bootstrap.bundle.min.js"></script>
    <link href="<%=request.getContextPath()%>/static/css/app.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
    <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">Precision Medicine Matching System</a>
</nav>

<div class="container-fluid">
    <div class="row">
        <jsp:include page="nav.jsp">
            <jsp:param name="active" value="drugs" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <div>
                    <h2>Drug Detail</h2>
                    <p class="text-muted mb-0">Review the full drug record and related pharmacogenomics knowledge.</p>
                </div>
                <div>
                    <c:choose>
                        <c:when test="${isSaved}">
                            <a href="removeSavedItem?itemType=DRUG&itemId=${drug.id}&returnTo=/drugDetail?id=${drug.id}" class="btn btn-outline-danger btn-sm">Remove from Saved</a>
                        </c:when>
                        <c:otherwise>
                            <a href="saveItem?itemType=DRUG&itemId=${drug.id}&returnTo=/drugDetail?id=${drug.id}" class="btn btn-outline-warning btn-sm">Save</a>
                        </c:otherwise>
                    </c:choose>
                    <a href="drugDetail?id=${drug.id}&explain=1" class="btn btn-outline-info btn-sm ml-2">Explain</a>
                    <a href="drugs" class="btn btn-outline-secondary btn-sm">Back to Drugs</a>
                </div>
            </div>

            <c:if test="${not empty explanation}">
                <div class="alert alert-info" role="alert">
                    <strong>Explanation:</strong> ${explanation}
                </div>
            </c:if>

            <div class="card shadow-sm mb-4">
                <div class="card-body">
                    <h4 class="card-title mb-3">${drug.name}</h4>
                    <dl class="row mb-0">
                        <dt class="col-sm-3">Drug ID</dt>
                        <dd class="col-sm-9">${drug.id}</dd>
                        <dt class="col-sm-3">Object Class</dt>
                        <dd class="col-sm-9">${drug.objCls}</dd>
                        <dt class="col-sm-3">Biomarker</dt>
                        <dd class="col-sm-9">${drug.biomarker}</dd>
                        <dt class="col-sm-3">Drug URL</dt>
                        <dd class="col-sm-9">
                            <c:choose>
                                <c:when test="${not empty drug.drugUrl}">
                                    <a href="${drug.drugUrl}" target="_blank" rel="noopener noreferrer">${drug.drugUrl}</a>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted">No external drug URL available.</span>
                                </c:otherwise>
                            </c:choose>
                        </dd>
                    </dl>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-6 mb-4">
                    <div class="card shadow-sm h-100">
                        <div class="card-header">Related Drug Labels</div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${!relatedDrugLabels.isEmpty()}">
                                    <ul class="mb-0 pl-3">
                                        <c:forEach items="${relatedDrugLabels}" var="item">
                                            <li class="mb-2">
                                                <a href="drugLabelDetail?id=${item.id}">${item.displayName}</a>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-light mb-0" role="alert">
                                        No related drug labels were found for this drug.
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6 mb-4">
                    <div class="card shadow-sm h-100">
                        <div class="card-header">Related Dosing Guidelines</div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${!relatedDosingGuidelines.isEmpty()}">
                                    <ul class="mb-0 pl-3">
                                        <c:forEach items="${relatedDosingGuidelines}" var="item">
                                            <li class="mb-2">
                                                <a href="dosingGuidelineDetail?id=${item.id}">${item.name}</a>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-light mb-0" role="alert">
                                        No related dosing guidelines were found for this drug.
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>
