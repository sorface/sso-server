package by.sorface.sso.web.services.redis;

import by.sorface.sso.web.services.redis.events.OAuth2AuthorizationCompleteEvent;
import by.sorface.sso.web.services.redis.events.OAuth2AuthorizationDeleteEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOAuth2AuthorizationListener implements OAuth2AuthorizationListener {

    private final RedisOAuth2AuthorizationDetailsService redisOAuth2AuthorizationDetailsService;

    @Override
    @EventListener(OAuth2AuthorizationCompleteEvent.class)
    public void complete(OAuth2AuthorizationCompleteEvent event) {
        final var authorization = event.authorization();

        if (redisOAuth2AuthorizationDetailsService.save(authorization)) {
            log.info("save authorization complete event with id '{}'", authorization.getId());
        } else {
            log.warn("save authorization complete event with id '{}' failed", authorization.getId());
        }
    }

    @Override
    @EventListener(OAuth2AuthorizationDeleteEvent.class)
    public void delete(OAuth2AuthorizationDeleteEvent event) {
        redisOAuth2AuthorizationDetailsService.deleteById(event.authorization().getId());
    }

}
