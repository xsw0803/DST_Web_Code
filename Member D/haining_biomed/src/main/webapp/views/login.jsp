<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Login</title>
    <link href="<%=request.getContextPath()%>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="<%=request.getContextPath()%>/static/jquery/jquery-3.4.1.js"></script>
    <script src="<%=request.getContextPath()%>/static/bootstrap/js/bootstrap.bundle.min.js"></script>
    <link href="<%=request.getContextPath()%>/static/css/app.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container">
    <div class="row justify-content-center" style="min-height: 100vh;">
        <div class="col-md-6 col-lg-4 align-self-center">
            <div class="text-center mb-4">
                <h2>Precision Medicine Matching System</h2>
                <p class="text-muted mb-0">Sign in to continue.</p>
            </div>
            <div class="card shadow-sm">
                <div class="card-body">
                    <c:if test="${param.registered == '1'}">
                        <div class="alert alert-success" role="alert">
                            Registration successful. Please sign in.
                        </div>
                    </c:if>
                    <c:if test="${error != null}">
                        <div class="alert alert-danger" role="alert">
                            ${error}
                        </div>
                    </c:if>
                    <form method="post" action="login">
                        <div class="form-group">
                            <label for="username">Username</label>
                            <input type="text" class="form-control" id="username" name="username" placeholder="Enter username">
                        </div>
                        <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" class="form-control" id="password" name="password" placeholder="Enter password">
                        </div>
                        <button type="submit" class="btn btn-primary btn-block">Login</button>
                    </form>
                    <div class="text-center mt-3">
                        <a href="register">Create an account</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
