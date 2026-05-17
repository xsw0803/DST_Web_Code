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
    <title>Matching Result</title>

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

        .result-toolbar {
            display: flex;
            flex-wrap: wrap;
            justify-content: flex-end;
            gap: 0.75rem;
            align-items: flex-start;
            margin-top: 0.5rem;
        }

        .toolbar-group {
            display: flex;
            flex-wrap: wrap;
            gap: 0.5rem;
            align-items: center;
            justify-content: flex-end;
        }

        .toolbar-group .btn {
            min-width: 152px;
        }

        .result-summary-alert {
            line-height: 1.55;
        }

        .matched-actions {
            display: flex;
            flex-direction: column;
            gap: 0.4rem;
            align-items: flex-start;
            min-width: 120px;
        }

        .matched-actions .btn {
            width: 100%;
        }

        @media (max-width: 991.98px) {
            .result-toolbar {
                justify-content: flex-start;
                align-items: stretch;
            }

            .toolbar-group {
                justify-content: flex-start;
            }

            .toolbar-group .btn {
                min-width: 0;
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
                    <h2>Matching Result</h2>
                    <p class="text-muted mb-0">Review matched pharmacogenomics drug labels for the uploaded sample.</p>
                </div>
                <div class="result-toolbar">
                    <div class="toolbar-group">
                        <a href="matching?sampleId=${sample.id}&explainSample=1" class="btn btn-outline-info btn-sm">Explain This Sample</a>
                        <a href="samples" class="btn btn-outline-secondary btn-sm">Sample History</a>
                    </div>
                    <div class="toolbar-group">
                        <a href="matchingIndex" class="btn btn-outline-primary btn-sm">Upload New Sample</a>
                        <a href="exportMatching?sampleId=${sample.id}" class="btn btn-outline-success btn-sm">Export CSV</a>
                        <a href="exportMatchingPdf?sampleId=${sample.id}" class="btn btn-success btn-sm">Export PDF</a>
                    </div>
                </div>
            </div>

            <c:if test="${not empty sampleExplanation}">
                <div class="alert alert-info mb-4 result-summary-alert" role="alert">
                    <strong>Sample Explanation:</strong> ${sampleExplanation}
                </div>
            </c:if>

            <div class="row mb-4">
                <div class="col-md-8">
                    <div class="alert alert-info" role="alert">
                        <h4 class="alert-heading">Sample #${sample.id}</h4>
                        <div>Uploaded at: ${sample.createdAt}</div>
                        <div>Uploaded by: ${sample.uploadedBy}</div>
                        <div>Detected genes: ${refGenes.size()}</div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card shadow-sm text-center">
                        <div class="card-body">
                            <h5 class="card-title">Matched Drug Labels</h5>
                            <div class="display-4">${matched.size()}</div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="card shadow-sm mb-4">
                <div class="card-header">
                    Matched Drug Labels
                </div>
                <div class="card-body">
                    <div class="input-group mb-3">
                        <input type="text" class="form-control" id="matchedResultFilter"
                               placeholder="Filter matched labels by name, source, or summary">
                        <div class="input-group-append">
                            <button class="btn btn-outline-secondary" type="button" id="clearMatchedResultFilter">Clear</button>
                        </div>
                    </div>

                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <small class="text-muted">
                            Showing <span id="visibleMatchedCount">${matched.size()}</span> of ${matched.size()} matched result(s)
                        </small>
                    </div>

                    <c:choose>
                    <c:when test="${!matched.isEmpty()}">
                            <div class="table-responsive">
                                <table class="table table-striped table-sm">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Name</th>
                                        <th>Source</th>
                                        <th>Actions</th>
                                        <th>Summary</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${matched}" var="item" varStatus="loop">
                                        <tr class="matched-result-row">
                                        <td>${loop.index + 1}</td>
                                            <td><a href="drugLabelDetail?id=${item.id}&returnTo=matching&sampleId=${sample.id}">${item.displayName}</a></td>
                                            <td>
                                                <span class="badge badge-info">${item.source}</span>
                                            </td>
                                            <td>
                                                <div class="matched-actions">
                                                    <c:choose>
                                                        <c:when test="${savedMatchedLabelIds.contains(item.id)}">
                                                            <a href="removeSavedItem?itemType=MATCH_RESULT&itemId=${item.id}&returnTo=/matching?sampleId=${sample.id}" class="btn btn-outline-danger btn-sm">Remove</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="saveItem?itemType=MATCH_RESULT&itemId=${item.id}&returnTo=/matching?sampleId=${sample.id}" class="btn btn-outline-warning btn-sm">Save</a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <a href="drugLabelDetail?id=${item.id}&returnTo=matching&sampleId=${sample.id}&explain=1" class="btn btn-outline-info btn-sm">Explain</a>
                                                </div>
                                            </td>
                                            <td>${item.summaryMarkdown}</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-warning mb-0" role="alert">
                                No matched drug labels were found for this sample.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </main>
    </div>
</div>
<script>
    $(function () {
        function updateMatchedResultFilter() {
            var keyword = $('#matchedResultFilter').val().toLowerCase();
            var visibleCount = 0;

            $('.matched-result-row').each(function () {
                var rowText = $(this).text().toLowerCase();
                var visible = rowText.indexOf(keyword) !== -1;
                $(this).toggle(visible);
                if (visible) {
                    visibleCount++;
                }
            });

            $('#visibleMatchedCount').text(visibleCount);
        }

        $('#matchedResultFilter').on('input', updateMatchedResultFilter);

        $('#clearMatchedResultFilter').on('click', function () {
            $('#matchedResultFilter').val('');
            updateMatchedResultFilter();
        });
    });
</script>
</body>
</html>
