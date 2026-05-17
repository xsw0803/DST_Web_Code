package cn.edu.zju.servlet;

import cn.edu.zju.bean.User;
import cn.edu.zju.dao.UserDao;
import cn.edu.zju.filter.AuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SigninServlet",  urlPatterns = "/signin")
public class SigninServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect");

        if (username != null) {
            username = username.trim();
        }

        UserDao userDao = new UserDao();
        User user = userDao.findByUsernameAndPassword(username, password);
        if (user != null) {
            request.getSession().setAttribute(AuthenticationFilter.SESSION_USER_ID, user.getId());
            request.getSession().setAttribute(AuthenticationFilter.SESSION_USERNAME, user.getUsername());
            request.getSession().setAttribute(AuthenticationFilter.SESSION_ROLE, user.getRole());
            request.getSession().setAttribute(AuthenticationFilter.SESSION_DISPLAY_NAME, user.getDisplayName());

            String safeRedirect = normalizeRedirect(redirect, request.getContextPath());
            if (safeRedirect != null) {
                response.sendRedirect(safeRedirect);
            } else {
                response.sendRedirect(request.getContextPath() + "/index");
            }
        } else {
            request.setAttribute("error", "Username or password is incorrect.");
            request.setAttribute("redirect", redirect);
            request.getRequestDispatcher("/views/signin.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role = (String) request.getSession().getAttribute(AuthenticationFilter.SESSION_ROLE);
        if (role != null && !role.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }
        request.setAttribute("redirect", request.getParameter("redirect"));
        request.setAttribute("registered", "1".equals(request.getParameter("registered")));
        request.getRequestDispatcher("/views/signin.jsp").forward(request, response);
    }

    private String normalizeRedirect(String redirect, String contextPath) {
        if (redirect == null) {
            return null;
        }
        String trimmed = redirect.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        if (!trimmed.startsWith("/")) {
            return null;
        }
        if (!trimmed.startsWith(contextPath + "/") && !trimmed.equals(contextPath)) {
            return null;
        }
        return trimmed;
    }
}
