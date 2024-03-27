package by.sorface.sso.web.config.handlers;

import by.sorface.sso.web.records.principals.DefaultPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSessionLogoutHandler implements LogoutHandler {

    private final RedisIndexedSessionRepository redisIndexedSessionRepository;

    private final RedisSessionProperties redisSessionProperties;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
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
