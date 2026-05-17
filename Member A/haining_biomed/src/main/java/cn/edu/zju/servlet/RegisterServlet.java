package cn.edu.zju.servlet;

import cn.edu.zju.dao.UserDao;
import cn.edu.zju.filter.AuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role = (String) request.getSession().getAttribute(AuthenticationFilter.SESSION_ROLE);
        if (role != null && !role.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = trimToEmpty(request.getParameter("username"));
        String displayName = trimToEmpty(request.getParameter("displayName"));
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String role = normalizeRole(request.getParameter("role"));

        if (username.isEmpty() || password == null || password.isEmpty()
                || confirmPassword == null || confirmPassword.isEmpty()) {
            request.setAttribute("error", "Please fill in all required fields.");
            preserveInput(request, username, displayName, role);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            preserveInput(request, username, displayName, role);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        if (role.isEmpty()) {
            request.setAttribute("error", "Please choose a valid role.");
            preserveInput(request, username, displayName, role);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        UserDao userDao = new UserDao();
        if (userDao.existsByUsername(username)) {
            request.setAttribute("error", "Username already exists.");
            preserveInput(request, username, displayName, role);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        int userId = userDao.saveUser(username, password, role, displayName);
        if (userId <= 0) {
            request.setAttribute("error", "Registration failed. Please try again.");
            preserveInput(request, username, displayName, role);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/signin?registered=1");
    }

    private void preserveInput(HttpServletRequest request, String username, String displayName, String role) {
        request.setAttribute("username", username);
        request.setAttribute("displayName", displayName);
        request.setAttribute("role", role);
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeRole(String role) {
        String normalized = trimToEmpty(role).toLowerCase();
        if ("patient".equals(normalized) || "professional".equals(normalized)) {
            return normalized;
        }
        return "";
    }
}
