<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Register - Precision Medicine Matching System</title>
    <link href="<%=request.getContextPath()%>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="<%=request.getContextPath()%>/static/jquery/jquery-3.4.1.js"></script>
    <script src="<%=request.getContextPath()%>/static/bootstrap/js/bootstrap.bundle.min.js"></script>
    <link href="<%=request.getContextPath()%>/static/css/style.css?v=20260513a" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/static/css/signin.css?v=20260513a" rel="stylesheet">
</head>
<body>
<div class="signin-wrapper">
    <div class="signin-card">
        <div class="signin-brand">Precision Medicine Matching System</div>
        <h1 class="signin-title">Create account</h1>
        <p class="signin-subtitle">Register a patient or professional account to access mutation upload, matching, and role-based interpretation.</p>

        <c:if test="${not empty error}">
            <div class="signin-alert signin-alert-error">${error}</div>
        </c:if>

        <form class="signin-form" method="post" action="<%=request.getContextPath()%>/register">
            <label class="signin-label" for="usernameInput">Username *</label>
            <input type="text" id="usernameInput" class="form-control signin-input" name="username" value="${username}" placeholder="Enter username" required autofocus>

            <label class="signin-label" for="displayNameInput">Display name</label>
            <input type="text" id="displayNameInput" class="form-control signin-input" name="displayName" value="${displayName}" placeholder="Optional display name">

            <label class="signin-label" for="passwordInput">Password *</label>
            <input type="password" id="passwordInput" class="form-control signin-input" name="password" placeholder="Enter password" required>

            <label class="signin-label" for="confirmPasswordInput">Confirm password *</label>
            <input type="password" id="confirmPasswordInput" class="form-control signin-input" name="confirmPassword" placeholder="Re-enter password" required>

            <label class="signin-label" for="roleInput">Role *</label>
            <select id="roleInput" class="form-control signin-input" name="role" required>
                <option value="">Select role</option>
                <option value="patient" ${role == 'patient' ? 'selected' : ''}>Patient</option>
                <option value="professional" ${role == 'professional' ? 'selected' : ''}>Professional</option>
            </select>

            <button class="btn btn-primary btn-block signin-submit" type="submit">Register</button>
        </form>

        <div class="signin-role-note">
            <strong>Account policy</strong>
            <div>Self-registration is available for Patient and Professional roles.</div>
            <div>Admin accounts are provisioned separately by system administrators.</div>
        </div>

        <div class="signin-footer-note">
            Already have an account?
            <a href="<%=request.getContextPath()%>/signin">Sign in</a>
        </div>
    </div>
</div>
</body>
</html>
