<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Sample History</title>
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
            <jsp:param name="active" value="sample_history" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>Sample History</h2>
            </div>
            <p class="page-description">Track uploaded mutation samples and review their matching completion status.</p>

            <c:if test="${empty rows}">
                <div class="alert alert-light border">No uploaded samples found.</div>
            </c:if>
            <c:if test="${not empty rows}">
                <div class="table-responsive">
                    <table class="table table-striped table-sm">
                        <thead>
                        <tr>
                            <th>Sample ID</th>
                            <th>File Name</th>
                            <th>Upload Time</th>
                            <th>Uploaded By</th>
                            <th>Total Variants</th>
                            <th>Matched Genes</th>
                            <th>Matched Drugs</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${rows}" var="row">
                            <tr>
                                <td>S${row.sampleId}</td>
                                <td>${row.fileName}</td>
                                <td>${row.uploadTime}</td>
                                <td>${row.uploadedBy}</td>
                                <td>${row.variantCount}</td>
                                <td>${row.matchedGeneCount}</td>
                                <td>${row.matchedDrugCount}</td>
                                <td>
                                    <span class="badge ${row.status == 'Completed' ? 'badge-success' : 'badge-secondary'}">${row.status}</span>
                                </td>
                                <td>
                                    <a href="<%=request.getContextPath()%>/mutationResult?sampleId=${row.sampleId}" class="mr-2">View Result</a>
                                    <a href="<%=request.getContextPath()%>/mutationExport?sampleId=${row.sampleId}&format=csv" class="mr-2">CSV</a>
                                    <a href="<%=request.getContextPath()%>/mutationExport?sampleId=${row.sampleId}&format=txt">Summary</a>
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
