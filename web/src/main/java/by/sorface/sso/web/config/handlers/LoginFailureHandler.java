package by.sorface.sso.web.config.handlers;

import by.sorface.sso.web.config.properties.MvcEndpointProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private static final BiFunction<String, String, String> BUILDER_URL = (error, url) ->
            url + "?error=" + UriUtils.encode(error, StandardCharsets.US_ASCII);
    private final MvcEndpointProperties mvcEndpointProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        final var errorMessageCode = (exception instanceof BadCredentialsException) ? "password.or.username.invalid" : "authentication.invalid";

        String redirectUri = BUILDER_URL.apply(errorMessageCode, mvcEndpointProperties.getUrlPageSignIn());

        response.sendRedirect(redirectUri);
    }

}
