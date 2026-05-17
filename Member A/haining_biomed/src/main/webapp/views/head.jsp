<%--
  Created by IntelliJ IDEA.
  User: hello
  Date: 2019-12-3
  Time: 17:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
    <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">Precision Medicine Matching System</a>
    <ul class="navbar-nav px-3">
        <li class="nav-item text-nowrap">
            <c:if test="${sessionScope.username == null}">
                <a class="nav-link d-inline-block" href="<%=request.getContextPath()%>/signin">Sign in</a>
                <a class="nav-link d-inline-block ml-2" href="<%=request.getContextPath()%>/register">Register</a>
            </c:if>
            <c:if test="${sessionScope.username != null}">
                <span class="nav-link user-status">
                    Logged in as: ${empty sessionScope.display_name ? sessionScope.username : sessionScope.display_name}
                    (${sessionScope.role})
                </span>
                <a class="nav-link logout-link" href="<%=request.getContextPath()%>/logout">Logout</a>
            </c:if>
        </li>
    </ul>
</nav>
