<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Drug Label Detail</title>
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
            <jsp:param name="active" value="drug_labels" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <div>
                    <h2>Drug Label Detail</h2>
                    <p class="text-muted mb-0">Review the full clinical annotation for this drug label.</p>
                </div>
                <div>
                    <c:choose>
                        <c:when test="${isSaved}">
                            <a href="removeSavedItem?itemType=${savedItemType}&itemId=${drugLabel.id}&returnTo=/drugLabelDetail?id=${drugLabel.id}" class="btn btn-outline-danger btn-sm">Remove from Saved</a>
                        </c:when>
                        <c:otherwise>
                            <a href="saveItem?itemType=${saveItemType}&itemId=${drugLabel.id}&returnTo=/drugLabelDetail?id=${drugLabel.id}" class="btn btn-outline-warning btn-sm">Save</a>
                        </c:otherwise>
                    </c:choose>
                    <a href="drugLabelDetail?id=${drugLabel.id}&explain=1" class="btn btn-outline-info btn-sm ml-2">Explain</a>
                    <c:choose>
                        <c:when test="${returnToMatching}">
                            <a href="matching?sampleId=${sampleId}" class="btn btn-outline-secondary btn-sm ml-2">Back to Matching Result</a>
                        </c:when>
                        <c:otherwise>
                            <a href="drugLabels" class="btn btn-outline-secondary btn-sm ml-2">Back to Drug Labels</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <c:if test="${not empty explanation}">
                <div class="alert alert-info" role="alert">
                    <strong>Explanation:</strong> ${explanation}
                </div>
            </c:if>

            <div class="card shadow-sm mb-4">
                <div class="card-body">
                    <h4 class="card-title mb-3">${drugLabel.displayName}</h4>
                    <dl class="row">
                        <dt class="col-sm-3">Label ID</dt>
                        <dd class="col-sm-9">${drugLabel.id}</dd>
                        <dt class="col-sm-3">Source</dt>
                        <dd class="col-sm-9">${drugLabel.source}</dd>
                        <dt class="col-sm-3">Drug ID</dt>
                        <dd class="col-sm-9">${drugLabel.drugId}</dd>
                        <dt class="col-sm-3">Dosing Information</dt>
                        <dd class="col-sm-9">${drugLabel.dosingInformation}</dd>
                        <dt class="col-sm-3">Alternate Drug Available</dt>
                        <dd class="col-sm-9">${drugLabel.alternateDrugAvailable}</dd>
                    </dl>

                    <c:if test="${not empty relatedDrug}">
                        <div class="mb-3">
                            <strong>Related Drug:</strong>
                            <a href="drugDetail?id=${relatedDrug.id}">${relatedDrug.name}</a>
                        </div>
                    </c:if>

                    <div class="mb-3">
                        <h5>Summary</h5>
                        <p class="mb-0">${drugLabel.summaryMarkdown}</p>
                    </div>

                    <div class="mb-3">
                        <h5>Prescribing Markdown</h5>
                        <p class="mb-0">${drugLabel.prescribingMarkdown}</p>
                    </div>

                    <div class="mb-0">
                        <h5>Text Markdown</h5>
                        <p class="mb-0">${drugLabel.textMarkdown}</p>
                    </div>
                </div>
            </div>

            <div class="card shadow-sm mb-4">
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
                                No related dosing guidelines were found for this label.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>
