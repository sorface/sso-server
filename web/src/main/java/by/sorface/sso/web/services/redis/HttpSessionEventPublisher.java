package by.sorface.sso.web.services.redis;

import by.sorface.sso.web.dao.nosql.redis.models.RedisOAuth2Authorization;
import by.sorface.sso.web.records.principals.DefaultPrincipal;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpSessionEventPublisher extends org.springframework.security.web.session.HttpSessionEventPublisher {

    private final RedisOAuth2AuthorizationDetailsService authorizationDetailsService;

    private final RedisOAuth2AuthorizationService authorizationService;

    @EventListener
    public void sessionDestroyed(final HttpSessionEvent sessionEvent) {
        final HttpSession session = sessionEvent.getSession();

        log.debug("session id [{}] destroyed", session.getId());

        final var securityContext = (SecurityContext) sessionEvent.getSession().getAttribute("SPRING_SECURITY_CONTEXT");

        if (Objects.isNull(securityContext)) {
            log.debug("session id [{}] has security context is NULL", session.getId());
            return;
        }

        final Authentication authentication = securityContext.getAuthentication();

        if (Objects.isNull(authentication)) {
            log.debug("session id [{}] has authentication is NULL", session.getId());
            return;
        }

        final var principal = (DefaultPrincipal) authentication.getPrincipal();

        if (Objects.isNull(principal)) {
            log.debug("session id [{}] has authentication is NULL", session.getId());
            return;
        }

        final List<RedisOAuth2Authorization> principalAuthorizations = authorizationDetailsService.findByPrincipleId(principal.getId().toString());

        for (final var authorization : principalAuthorizations) {
            if (Objects.isNull(authorization)) {
                continue;
            }

            final OAuth2Authorization auth2Authorization = authorizationService.findById(authorization.getId());

            if (Objects.isNull(auth2Authorization)) {
                authorizationDetailsService.deleteById(authorization.getId());
            } else {
                authorizationService.remove(auth2Authorization);
            }
        }
    }

}
