package by.sorface.ssoserver.config.handlers;

import by.sorface.ssoserver.records.SfPrincipal;
import by.sorface.ssoserver.utils.json.Json;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Map;

@Slf4j
public class AuthLoginSuccessHandler extends AbstractAuthLoginHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        final SfPrincipal principal = (SfPrincipal) authentication.getPrincipal();

        final var json = Json.stringify(Map.of(
                "id", principal.getId(),
                "email", principal.getEmail(),
                "firstName", principal.getFirstName(),
                "lastName", principal.getLastName()
        ));

        writeBody(json, HttpStatus.OK, response);
    }
}
