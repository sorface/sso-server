package by.sorface.ssoserver.config;

import by.sorface.ssoserver.records.SorfaceUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class SorfaceAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final String locationUrl;

    private final String headerName;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final SorfaceUser principal = (SorfaceUser) authentication.getPrincipal();

        log.debug("User sign in success with id [{}], email [{}], username: [{}]", principal.getId(), principal.getEmail(),
                principal.getUsername());

        response.setHeader(headerName, locationUrl);
    }

}
