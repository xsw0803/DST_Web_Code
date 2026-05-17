<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Matching Result PDF</title>
    <link href="<%=request.getContextPath()%>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <style>
        body {
            background: #f8f9fa;
            color: #212529;
        }

        .report-shell {
            max-width: 1080px;
            margin: 24px auto;
            background: #ffffff;
            padding: 32px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
        }

        .report-header {
            border-bottom: 2px solid #dee2e6;
            padding-bottom: 16px;
            margin-bottom: 24px;
        }

        .report-kicker {
            color: #6c757d;
            font-size: 0.95rem;
            margin-bottom: 6px;
        }

        .report-title {
            font-size: 2.2rem;
            font-weight: 700;
            margin-bottom: 8px;
        }

        .report-actions {
            display: flex;
            flex-wrap: wrap;
            gap: 0.75rem;
            justify-content: flex-end;
            margin-bottom: 20px;
        }

        .report-meta dt {
            font-weight: 600;
            color: #495057;
        }

        .report-meta dd {
            margin-bottom: 0.75rem;
        }

        .report-summary {
            background: #e8f7fb;
            border: 1px solid #bee5eb;
            border-radius: 6px;
            padding: 16px 18px;
            line-height: 1.65;
        }

        .report-table td,
        .report-table th {
            vertical-align: top;
        }

        @media print {
            body {
                background: #ffffff;
            }

            .report-shell {
                margin: 0;
                max-width: none;
                box-shadow: none;
                padding: 0;
            }

            .report-actions {
                display: none;
            }
        }
    </style>
</head>
<body>
<div class="report-shell">
    <div class="report-actions">
        <button type="button" class="btn btn-primary" onclick="window.print()">Print / Save as PDF</button>
        <a href="matching?sampleId=${sample.id}" class="btn btn-outline-secondary">Back to Matching Result</a>
    </div>

    <div class="report-header">
        <div class="report-kicker">Precision Medicine Matching System</div>
        <div class="report-title">Matching Result Report</div>
        <p class="text-muted mb-0">A print-friendly summary of the uploaded sample, detected genes, AI explanation, and matched pharmacogenomics labels.</p>
    </div>

    <div class="row mb-4">
        <div class="col-md-6">
            <dl class="report-meta">
                <dt>Sample ID</dt>
                <dd>#${sample.id}</dd>
                <dt>Uploaded By</dt>
                <dd>${sample.uploadedBy}</dd>
                <dt>Uploaded At</dt>
                <dd>${sample.createdAt}</dd>
            </dl>
        </div>
        <div class="col-md-6">
            <dl class="report-meta">
                <dt>Detected Genes</dt>
                <dd>${refGenes.size()}</dd>
                <dt>Matched Drug Labels</dt>
                <dd>${matched.size()}</dd>
                <dt>Gene List</dt>
                <dd>
                    <c:forEach items="${refGenes}" var="gene" varStatus="loop">
                        ${gene}<c:if test="${!loop.last}">, </c:if>
                    </c:forEach>
                </dd>
            </dl>
        </div>
    </div>

    <div class="mb-4">
        <h4>AI Sample Explanation</h4>
        <div class="report-summary">
            ${sampleExplanation}
        </div>
    </div>

    <div>
        <h4 class="mb-3">Matched Drug Labels</h4>
        <div class="table-responsive">
            <table class="table table-bordered table-sm report-table">
                <thead class="thead-light">
                <tr>
                    <th style="width: 5%;">#</th>
                    <th style="width: 23%;">Label Name</th>
                    <th style="width: 18%;">Source</th>
                    <th>Summary</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${matched}" var="item" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>${item.displayName}</td>
                        <td>${item.source}</td>
                        <td>${item.summaryMarkdown}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script>
    setTimeout(function () {
        window.print();
    }, 250);
</script>
</body>
</html>
