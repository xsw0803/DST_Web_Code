<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Mutation Annotation Search</title>
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
            <jsp:param name="active" value="mutation_search" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>Mutation Annotation Search</h2>
            </div>
            <p class="page-description">Search uploaded ANNOVAR-style annotation records by sample, gene, rsID, chromosome, or genomic position.</p>

            <div class="upload-main-card mb-3">
                <form method="get" action="<%=request.getContextPath()%>/mutationSearch">
                    <div class="upload-section-title">Main search</div>
                    <div class="form-row align-items-end">
                        <div class="col-lg-4 col-md-6 mb-2">
                            <label class="upload-label mb-1">Sample ID</label>
                            <input class="form-control" type="text" name="sampleId" placeholder="Sample ID, e.g. 10" value="${sampleId}">
                        </div>
                        <div class="col-lg-4 col-md-6 mb-2">
                            <label class="upload-label mb-1">Gene</label>
                            <input class="form-control" type="text" name="gene" placeholder="Gene symbol, e.g. DPYD" value="${gene}">
                        </div>
                        <div class="col-lg-4 col-md-6 mb-2">
                            <label class="upload-label mb-1">rsID</label>
                            <input class="form-control" type="text" name="rsId" placeholder="rsID, e.g. demo_rs_DPYD" value="${rsId}">
                        </div>
                    </div>

                    <c:if test="${canUseAdvancedFilters}">
                        <div class="upload-section-title mt-2">Advanced filters</div>
                        <div class="form-row align-items-end">
                            <div class="col-lg-3 col-md-6 mb-2">
                                <label class="upload-label mb-1">Chromosome</label>
                                <input class="form-control" type="text" name="chr" placeholder="Chr, e.g. 1" value="${chr}">
                            </div>
                            <div class="col-lg-3 col-md-6 mb-2">
                                <label class="upload-label mb-1">Position</label>
                                <input class="form-control" type="text" name="position" placeholder="Position, e.g. 100006" value="${position}">
                            </div>
                        </div>
                    </c:if>

                    <div class="mt-2">
                        <button type="submit" class="btn btn-primary btn-sm mr-2">Search</button>
                        <a class="btn btn-outline-secondary btn-sm" href="<%=request.getContextPath()%>/mutationSearch">Reset</a>
                    </div>
                </form>

                <div class="mt-3 upload-muted">
                    Try examples:
                    <a class="ml-1 mr-2" href="<%=request.getContextPath()%>/mutationSearch?gene=DPYD">DPYD</a>
                    <a class="mr-2" href="<%=request.getContextPath()%>/mutationSearch?gene=CYP2C19">CYP2C19</a>
                    <a class="mr-2" href="<%=request.getContextPath()%>/mutationSearch?sampleId=10">10</a>
                    <a href="<%=request.getContextPath()%>/mutationSearch?rsId=demo_rs_DPYD">demo_rs_DPYD</a>
                </div>
            </div>

            <div class="disclaimer-box mb-3">
                This page searches uploaded annotation records. To view pharmacogenetic matching and interpretation, open the corresponding sample result.
            </div>

            <c:if test="${hasQuery}">
                <p class="drug-result-summary mb-2">Showing ${resultCount} annotation record(s)</p>
            </c:if>

            <c:if test="${hasQuery and empty variants}">
                <div class="alert alert-light border">
                    No mutation annotation records were found. Try another sample ID, gene symbol, or rsID.
                </div>
            </c:if>
            <c:if test="${not hasQuery}">
                <div class="alert alert-light border">
                    Enter at least one search field to find uploaded annotation records.
                </div>
            </c:if>

            <c:if test="${not empty variants}">
                <div class="table-responsive">
                    <table class="table table-striped table-sm guideline-table mutation-search-table">
                        <colgroup>
                            <col class="col-sample">
                            <col class="col-gene">
                            <col class="col-rsid">
                            <col class="col-location">
                            <col class="col-refalt">
                            <col class="col-function">
                            <col class="col-exonic">
                            <col class="col-status">
                            <col class="col-result">
                        </colgroup>
                        <thead>
                        <tr>
                            <th>Sample</th>
                            <th>Gene</th>
                            <th>rsID</th>
                            <th>Location</th>
                            <th>Ref / Alt</th>
                            <th>Function</th>
                            <th>Exonic Function</th>
                            <th>Status</th>
                            <th>Result</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${variants}" var="item">
                            <tr>
                                <td class="cell-sample">S${item.sampleId}</td>
                                <td class="cell-gene">${item.geneRefGene}</td>
                                <td class="cell-rsid" title="${item.avsnp150}">${item.avsnp150}</td>
                                <td class="cell-location" title="chr${item.chr}:${item.start}-${item.end}">chr${item.chr}:${item.start}-${item.end}</td>
                                <td class="cell-refalt">${item.ref} &gt; ${item.alt}</td>
                                <td class="cell-function">${item.funcRefGene}</td>
                                <td class="cell-exonic" title="${item.exonicFuncRefGene}">${item.exonicFuncRefGene}</td>
                                <td class="cell-status">
                                    <span class="badge ${item.functionalStatus == 'Potentially relevant' ? 'badge-success' : 'badge-secondary'}">
                                        ${item.functionalStatus}
                                    </span>
                                </td>
                                <td class="cell-result">
                                    <a class="btn btn-outline-primary btn-sm evidence-btn" href="<%=request.getContextPath()%>/mutationResult?sampleId=${item.sampleId}">
                                        View Result
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>

            <div class="mt-4 disclaimer-box">
                Search results are mutation annotation references and should be interpreted by qualified healthcare professionals.
            </div>
        </main>
    </div>
</div>
</body>
</html>
