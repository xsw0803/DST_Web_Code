package cn.edu.zju.controller;

import cn.edu.zju.bean.UserAccount;
import cn.edu.zju.dao.UserAccountDao;
import cn.edu.zju.servlet.DispatchServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthController {

    public static final String SESSION_USER = "loginUser";

    private final UserAccountDao userAccountDao = new UserAccountDao();

    public void register(DispatchServlet.Dispatcher dispatcher) {
        dispatcher.registerGetMapping("/login", this::loginPage);
        dispatcher.registerPostMapping("/login", this::login);
        dispatcher.registerGetMapping("/register", this::registerPage);
        dispatcher.registerPostMapping("/register", this::registerAccount);
        dispatcher.registerGetMapping("/logout", this::logout);
    }

    public void loginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute(SESSION_USER) != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = trimToNull(request.getParameter("username"));
        String password = trimToNull(request.getParameter("password"));

        if (username == null || password == null) {
            request.setAttribute("error", "Username and password can not be blank.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        UserAccount userAccount = userAccountDao.findByUsername(username);
        if (userAccount == null || !userAccount.getPassword().equals(hashPassword(password))) {
            request.setAttribute("error", "Invalid username or password.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        request.getSession().setAttribute(SESSION_USER, userAccount.getUsername());
        response.sendRedirect(request.getContextPath() + "/");
    }

    public void registerPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute(SESSION_USER) != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    public void registerAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = trimToNull(request.getParameter("username"));
        String password = trimToNull(request.getParameter("password"));
        String confirmPassword = trimToNull(request.getParameter("confirm_password"));

        if (username == null || password == null || confirmPassword == null) {
            request.setAttribute("error", "Username and password can not be blank.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Password and confirm password do not match.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        if (userAccountDao.existsByUsername(username)) {
            request.setAttribute("error", "Username already exists.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        int userId = userAccountDao.save(username, hashPassword(password));
        if (userId <= 0) {
            request.setAttribute("error", "Registration failed. Please check whether the user_account table exists and try again.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/login?registered=1");
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte b : hashed) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
