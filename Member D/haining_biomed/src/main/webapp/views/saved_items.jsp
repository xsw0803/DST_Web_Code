<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Saved Items</title>
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
            <jsp:param name="active" value="saved_items" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <div>
                    <h2>Saved Items</h2>
                    <p class="text-muted mb-0">Review your bookmarked drugs, labels, dosing guidelines, and matched result items.</p>
                </div>
            </div>

            <div class="card shadow-sm">
                <div class="card-body">
                    <c:choose>
                        <c:when test="${!savedItems.isEmpty()}">
                            <div class="table-responsive">
                                <table class="table table-striped table-sm mb-0">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Type</th>
                                        <th>Name</th>
                                        <th>Saved At</th>
                                        <th>Action</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${savedItems}" var="item" varStatus="loop">
                                        <tr>
                                            <td>${loop.index + 1}</td>
                                            <td>${item.typeLabel}</td>
                                            <td><a href="${item.detailUrl}">${item.displayName}</a></td>
                                            <td>${item.createdAt}</td>
                                            <td>
                                                <a href="removeSavedItem?itemType=${item.itemType}&itemId=${item.itemId}&returnTo=/savedItems" class="btn btn-outline-danger btn-sm">Remove</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-warning mb-0" role="alert">
                                You have not saved any items yet.
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
