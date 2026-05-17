package cn.edu.zju.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {
        "/mutationUpload",
        "/mutationResult",
        "/mutationSearch",
        "/mutationExport",
        "/sampleHistory",
        "/admin",
        "/pgxVariants"
})
public class AuthenticationFilter implements Filter {

    public static final String SESSION_USER_ID = "user_id";
    public static final String SESSION_USERNAME = "username";
    public static final String SESSION_ROLE = "role";
    public static final String SESSION_DISPLAY_NAME = "display_name";

    public static final String ROLE_PATIENT = "patient";
    public static final String ROLE_PROFESSIONAL = "professional";
    public static final String ROLE_ADMIN = "admin";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String role = null;
        if (session != null) {
            Object roleAttr = session.getAttribute(SESSION_ROLE);
            if (roleAttr != null) {
                role = roleAttr.toString();
            }
        }

        if (role == null || role.trim().isEmpty()) {
            redirectToSignin(httpRequest, httpResponse);
            return;
        }

        String uri = httpRequest.getRequestURI();
        if (uri.endsWith("/admin") || uri.endsWith("/sampleHistory") || uri.endsWith("/pgxVariants")) {
            if (!ROLE_ADMIN.equals(role)) {
                httpResponse.setContentType("text/html;charset=UTF-8");
                httpResponse.getWriter().write("You are not allowed to access this page. Please sign in as admin.");
                return;
            }
        }
        if (uri.endsWith("/mutationSearch")) {
            if (!(ROLE_PROFESSIONAL.equals(role) || ROLE_ADMIN.equals(role))) {
                httpResponse.setContentType("text/html;charset=UTF-8");
                httpResponse.getWriter().write("Mutation annotation search is available to professional and admin users only.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    private void redirectToSignin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contextPath = request.getContextPath();
        String requestUri = request.getRequestURI();
        String queryString = request.getQueryString();
        String target = requestUri + (queryString == null ? "" : "?" + queryString);
        response.sendRedirect(contextPath + "/signin?redirect=" + java.net.URLEncoder.encode(target, "UTF-8"));
    }
}
