package by.sorface.sso.web.config.security.csrf;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CsrfCookieFilter extends OncePerRequestFilter {

    /**
     * The doFilterInternal function is the function that actually does the work of filtering.
     * It takes in a request, response, and filter chain as parameters.
     * The filter chain represents all the filters that are applied to this particular request.
     * This function will be called for every single HTTP request made to your application (unless you explicitly exclude it).
     *
     * @param request     Get the csrf token from the request
     * @param response    Set the cookie
     * @param filterChain Invoke the next filter in the chain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final var csrfToken = (CsrfToken) request.getAttribute("_csrf");

        // Render the token value to a cookie by causing the deferred token to be loaded
        csrfToken.getToken();

        filterChain.doFilter(request, response);
    }

}