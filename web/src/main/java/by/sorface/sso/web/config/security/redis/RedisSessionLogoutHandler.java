package by.sorface.sso.web.config.security.redis;

import by.sorface.sso.web.records.principals.DefaultPrincipal;
import by.sorface.sso.web.services.redis.RedisOAuth2AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSessionLogoutHandler implements LogoutHandler {

    private final RedisIndexedSessionRepository redisIndexedSessionRepository;

    private final RedisSessionProperties redisSessionProperties;

    private final RedisOAuth2AuthorizationService redisOAuth2AuthorizationService;

    /**
     * The logout function is responsible for deleting the session from Redis.
     * It does this by first deleting the expires key, which is a key that contains
     * an expiration time for the session. This prevents any future access to this
     * session in Redis, even if it's still valid (i.e., not expired). Next, we delete
     * the index key and then finally delete the actual session data itself from Redis.
     *
     * @param request        Get the session id from the request
     * @param response       Set the cookie to expire
     * @param authentication Get the user's id
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (Objects.isNull(authentication)) {
            return;
        }

        if (authentication instanceof OAuth2AuthenticationToken auth2AuthenticationToken) {
            if (auth2AuthenticationToken.getPrincipal() instanceof DefaultPrincipal defaultPrincipal) {
                UUID id = defaultPrincipal.getId();
                String clientId = auth2AuthenticationToken.getAuthorizedClientRegistrationId();

                redisOAuth2AuthorizationService.remove(id, clientId);
            }
        }

        final var authorizedId = ((DefaultPrincipal) authentication.getPrincipal()).getId();

        final String expiresKey = redisSessionProperties.getNamespace() + ":expires:" + request.getRequestedSessionId();

        redisIndexedSessionRepository.getSessionRedisOperations().delete(expiresKey);

        log.info("deleted [session :expires key -> {}] for user [id -> {}]", expiresKey, authorizedId);

        redisIndexedSessionRepository.deleteById(request.getRequestedSessionId());

        log.info("deleted [session :index key -> {}] for user [id -> {}]", request.getRequestedSessionId(), authorizedId);

        final String key = redisSessionProperties.getNamespace() + ":sessions:" + request.getRequestedSessionId();

        redisIndexedSessionRepository.getSessionRedisOperations().delete(key);

        log.info("deleted [session :key -> {}] for user [id -> {}]", key, authorizedId);
    }

}
