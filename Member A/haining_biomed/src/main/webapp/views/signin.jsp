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
    <title>Precision Medicine Matching System</title>

    <!-- Bootstrap core CSS -->
    <link href="<%=request.getContextPath()%>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="<%=request.getContextPath()%>/static/jquery/jquery-3.4.1.js"></script>
    <script src="<%=request.getContextPath()%>/static/bootstrap/js/bootstrap.bundle.min.js"></script>
    <!-- Custom styles for this template -->
    <link href="<%=request.getContextPath()%>/static/css/style.css?v=20260512a" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/static/css/signin.css?v=20260512a" rel="stylesheet">
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
<div class="signin-wrapper">
    <div class="signin-card">
        <div class="signin-brand">Precision Medicine Matching System</div>
        <h1 class="signin-title">Sign in</h1>
        <p class="signin-subtitle">Access pharmacogenetic evidence browsing, mutation matching, and role-based interpretation.</p>

        <c:if test="${not empty redirect}">
            <div class="signin-alert signin-alert-info">
                Please sign in to continue to the requested page.
            </div>
        </c:if>
        <c:if test="${registered}">
            <div class="signin-alert signin-alert-info">
                Registration successful. Please sign in.
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="signin-alert signin-alert-error">
                Invalid username or password. Please try again.
            </div>
        </c:if>

        <form class="signin-form" method="post" action="<%=request.getContextPath()%>/signin">
            <input type="hidden" name="redirect" value="${redirect}">

            <label class="signin-label" for="usernameInput">Username</label>
            <input type="text" id="usernameInput" class="form-control signin-input" name="username" placeholder="Enter your username" required autofocus>

            <label class="signin-label" for="passwordInput">Password</label>
            <input type="password" id="passwordInput" class="form-control signin-input" name="password" placeholder="Enter your password" required>

            <button class="btn btn-primary btn-block signin-submit" type="submit">Sign in</button>
        </form>

        <div class="signin-role-note">
            <strong>Role-based access</strong>
            <div>Patient users can view simplified interpretations.</div>
            <div>Professional users can review detailed evidence.</div>
            <div>Administrators can access system management pages.</div>
        </div>

        <div class="signin-footer-note">
            <div class="mb-2">
                Don't have an account?
                <a href="<%=request.getContextPath()%>/register">Create one</a>
            </div>
            For educational and research use only. This system does not provide medical advice.
        </div>
    </div>
</div>
</body>
</html>
