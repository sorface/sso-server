package by.sorface.sso.web.config.handlers;

import by.sorface.sso.web.records.responses.OperationError;
import by.sorface.sso.web.services.locale.LocaleI18Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final LocaleI18Service localeI18Service;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        final var operationError = buildError(HttpStatus.UNAUTHORIZED, "exception.access.denied");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        try (final var responseStream = response.getOutputStream()) {
            final var mapper = new ObjectMapper();

            mapper.writeValue(responseStream, operationError);

            responseStream.flush();
        }
    }

    private OperationError buildError(final HttpStatus status, final String message) {
        return new OperationError(localeI18Service.getMessage(message), status.getReasonPhrase(), status.value());
    }

}
