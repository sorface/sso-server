package by.sorface.ssoserver.config.handlers;

import by.sorface.ssoserver.records.responses.OperationError;
import by.sorface.ssoserver.utils.json.Json;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.MimeTypeUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
public abstract class AbstractAuthLoginHandler {

    public void writeBody(final String json, final HttpStatus status, final HttpServletResponse response) {
        response.setStatus(status.value());
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (final var out = response.getWriter()) {
            out.print(json);
            out.flush();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }


}
