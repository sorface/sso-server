package by.sorface.sso.web.config.handlers;

import by.sorface.sso.web.config.properties.MvcEndpointProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String SPRING_SECURITY_SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";
    public static final String SORFACE_NEXT_LOCATION = "Sorface-Next-Location";
    private final RedisSessionRepository sessionRepository;

    private final MvcEndpointProperties mvcEndpointProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        final Session session = sessionRepository.findById(request.getRequestedSessionId());

        if (Objects.isNull(session)) {
            response.setHeader(SORFACE_NEXT_LOCATION, mvcEndpointProperties.getUriPageProfile());

            return;
        }

        final SavedRequest savedRequest = session.getRequiredAttribute(SPRING_SECURITY_SAVED_REQUEST);

        if (Objects.isNull(savedRequest)) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        final var targetUrlParameter = getTargetUrlParameter();

        if (isAlwaysUseDefaultTargetUrl() || (Objects.nonNull(targetUrlParameter) && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            this.sessionRepository.deleteById(request.getSession().getId());

            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        clearAuthenticationAttributes(request);

        final String targetUrl = savedRequest.getRedirectUrl();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

}
