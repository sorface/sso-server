package by.sorface.sso.web.config.security.handlers;

import by.sorface.sso.web.config.options.EndpointOptions;
import by.sorface.sso.web.services.locale.LocaleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationClientErrorHandler implements AuthenticationFailureHandler {

    private final EndpointOptions endpointOptions;

    private final LocaleService localeService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (exception instanceof OAuth2AuthorizationCodeRequestAuthenticationException) {
            final OAuth2Error error = ((OAuth2AuthorizationCodeRequestAuthenticationException) exception).getError();

            if (Objects.isNull(error)) {
                response.sendRedirect(endpointOptions.getUriPageFailure().concat("?error=").concat(exception.getMessage()));
                return;
            }

            final var message = localeService.getMessage(getLocalMessageByErrorCode(error));

            response.sendRedirect(endpointOptions.getUriPageFailure().concat("?message=").concat(message).concat("&description=").concat(error.getDescription()));

            return;
        }

        log.error("Error client -> ", exception);
        response.sendRedirect(endpointOptions.getUriPageFailure().concat("?error=").concat(exception.getMessage()));
    }

    private String getLocalMessageByErrorCode(final OAuth2Error error) {
        String errorMessage = Stream.of(
                        OAuth2ErrorCodes.INVALID_REQUEST,
                        OAuth2ErrorCodes.UNAUTHORIZED_CLIENT,
                        OAuth2ErrorCodes.ACCESS_DENIED,
                        OAuth2ErrorCodes.UNSUPPORTED_RESPONSE_TYPE,
                        OAuth2ErrorCodes.INVALID_SCOPE,
                        OAuth2ErrorCodes.INSUFFICIENT_SCOPE,
                        OAuth2ErrorCodes.INVALID_TOKEN,
                        OAuth2ErrorCodes.SERVER_ERROR,
                        OAuth2ErrorCodes.TEMPORARILY_UNAVAILABLE,
                        OAuth2ErrorCodes.INVALID_CLIENT,
                        OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE,
                        OAuth2ErrorCodes.UNSUPPORTED_TOKEN_TYPE,
                        OAuth2ErrorCodes.INVALID_REDIRECT_URI
                )
                .filter(it -> it.equalsIgnoreCase(error.getErrorCode()))
                .findFirst()
                .orElse(OAuth2ErrorCodes.SERVER_ERROR);

        return "message.client.error.".concat(errorMessage);
    }
}
