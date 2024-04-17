package by.sorface.sso.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CsrfController {

    private final CsrfTokenRepository csrfTokenRepository;

    /**
     * The setupCsrf function is a controller that returns the CSRF token.
     * This function is called by the frontend to get a new CSRF token when it needs one.
     * The frontend will call this function whenever it receives an HTTP 403 FORBIDDEN response from the backend, which indicates that its current CSRF token has expired and needs to be refreshed.
     *
     * @param request  Get the current session
     * @param response Set the cookie in the response header
     * @return A csrftoken object
     */
    @GetMapping("/csrf")
    public CsrfToken setupCsrf(HttpServletRequest request, HttpServletResponse response) {
        final var csrfToken = csrfTokenRepository.generateToken(request);

        csrfTokenRepository.saveToken(csrfToken, request, response);

        return csrfToken;
    }

}
