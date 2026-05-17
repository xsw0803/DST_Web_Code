<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>PGx Variant Records</title>
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
            <jsp:param name="active" value="pgx_variants" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>PGx Variant Records</h2>
            </div>
            <p class="page-description">Manage variant-level matching references used by the `pgx_variant` table.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <div class="upload-main-card mb-3">
                <form method="get" action="<%=request.getContextPath()%>/pgxVariants" class="form-inline">
                    <input class="form-control mr-2 mb-2" type="text" name="keyword" value="${keyword}" placeholder="Search by gene, rsID, variant, drug ID">
                    <button class="btn btn-primary btn-sm mr-2 mb-2" type="submit">Search</button>
                    <a class="btn btn-outline-secondary btn-sm mb-2" href="<%=request.getContextPath()%>/pgxVariants">Reset</a>
                </form>
            </div>

            <div class="upload-main-card mb-4">
                <h5 class="mb-3">Add PGx Variant Record</h5>
                <form method="post" action="<%=request.getContextPath()%>/pgxVariants">
                    <div class="form-row">
                        <div class="col-md-3 mb-2">
                            <label class="upload-label mb-1">Gene *</label>
                            <input class="form-control" type="text" name="gene" required>
                        </div>
                        <div class="col-md-3 mb-2">
                            <label class="upload-label mb-1">rsID *</label>
                            <input class="form-control" type="text" name="rsId" required>
                        </div>
                        <div class="col-md-3 mb-2">
                            <label class="upload-label mb-1">Variant Name</label>
                            <input class="form-control" type="text" name="variantName">
                        </div>
                        <div class="col-md-3 mb-2">
                            <label class="upload-label mb-1">Drug ID</label>
                            <input class="form-control" type="text" name="drugId">
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="col-md-3 mb-2">
                            <label class="upload-label mb-1">Function Status</label>
                            <input class="form-control" type="text" name="functionStatus">
                        </div>
                        <div class="col-md-3 mb-2">
                            <label class="upload-label mb-1">Phenotype</label>
                            <input class="form-control" type="text" name="phenotype">
                        </div>
                        <div class="col-md-3 mb-2">
                            <label class="upload-label mb-1">Evidence Source</label>
                            <input class="form-control" type="text" name="evidenceSource">
                        </div>
                        <div class="col-md-3 mb-2">
                            <label class="upload-label mb-1">Interpretation</label>
                            <input class="form-control" type="text" name="interpretation">
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary btn-sm mt-2">Add Record</button>
                </form>
            </div>

            <c:if test="${empty variants}">
                <div class="alert alert-light border">No PGx variant records found.</div>
            </c:if>
            <c:if test="${not empty variants}">
                <p class="drug-result-summary mb-2">Showing ${fn:length(variants)} PGx variant record(s)</p>
                <div class="table-responsive">
                    <table class="table table-striped table-sm guideline-table">
                        <thead>
                        <tr>
                            <th>Gene</th>
                            <th>rsID</th>
                            <th>Variant Name</th>
                            <th>Drug ID</th>
                            <th>Function Status</th>
                            <th>Phenotype</th>
                            <th>Evidence Source</th>
                            <th>Interpretation</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${variants}" var="item">
                            <tr>
                                <td>${item.gene}</td>
                                <td>${item.rsId}</td>
                                <td>${item.variantName}</td>
                                <td>${item.drugId}</td>
                                <td>${item.functionStatus}</td>
                                <td>${item.phenotype}</td>
                                <td>${item.evidenceSource}</td>
                                <td>
                                    <div class="label-summary-clamp" title="${item.interpretation}">
                                            ${item.interpretation}
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
        </main>
    </div>
</div>
</body>
</html>
