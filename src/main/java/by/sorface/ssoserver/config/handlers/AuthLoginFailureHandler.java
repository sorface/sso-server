package by.sorface.ssoserver.config.handlers;

import by.sorface.ssoserver.records.responses.OperationError;
import by.sorface.ssoserver.utils.json.Json;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Slf4j
public class AuthLoginFailureHandler extends AbstractAuthLoginHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final AuthenticationException exception) {
        final var badRequest = HttpStatus.BAD_REQUEST;

        final var operationError = OperationError.builder()
                .code(badRequest.value())
                .message(badRequest.getReasonPhrase())
                .details(exception.getMessage())
                .build();

        final var json = Json.stringify(operationError);

        writeBody(json, badRequest, response);
    }

}
