package by.sorface.sso.web.services.redis;

import by.sorface.sso.web.config.options.OAuth2Options;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public final class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final RedisTemplate<String, OAuth2Authorization> redisTemplate;

    private final ValueOperations<String, OAuth2Authorization> authorizations;

    private final OAuth2Options oAuth2Options;

    public RedisOAuth2AuthorizationService(final RedisTemplate<String, OAuth2Authorization> redisTemplate,
                                           final OAuth2Options oAuth2Options) {
        this.redisTemplate = redisTemplate;
        this.authorizations = redisTemplate.opsForValue();
        this.oAuth2Options = oAuth2Options;
    }

    private static boolean isComplete(OAuth2Authorization authorization) {
        return authorization.getAccessToken() != null;
    }

    private static boolean hasToken(OAuth2Authorization authorization, String token, @Nullable OAuth2TokenType tokenType) {
        if (Objects.isNull(tokenType)) {
            return matchesState(authorization, token) ||
                    matchesAuthorizationCode(authorization, token) ||
                    matchesAccessToken(authorization, token) ||
                    matchesIdToken(authorization, token) ||
                    matchesRefreshToken(authorization, token);
        }

        if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            return matchesState(authorization, token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            return matchesAuthorizationCode(authorization, token);
        } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            return matchesAccessToken(authorization, token);
        } else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
            return matchesIdToken(authorization, token);
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            return matchesRefreshToken(authorization, token);
        }

        return false;
    }

    private static boolean matchesState(OAuth2Authorization authorization, String token) {
        return token.equals(authorization.getAttribute(OAuth2ParameterNames.STATE));
    }

    private static boolean matchesAuthorizationCode(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                authorization.getToken(OAuth2AuthorizationCode.class);
        return authorizationCode != null && authorizationCode.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesAccessToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
                authorization.getToken(OAuth2AccessToken.class);
        return accessToken != null && accessToken.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesRefreshToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                authorization.getToken(OAuth2RefreshToken.class);
        return refreshToken != null && refreshToken.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesIdToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OidcIdToken> idToken =
                authorization.getToken(OidcIdToken.class);
        return idToken != null && idToken.getToken().getTokenValue().equals(token);
    }

    @Override
    public void save(final OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");

        log.info("save user's authorization object with id {}", authorization.getId());

        String key;

        if (isComplete(authorization)) {
            key = toComplete(authorization.getId());

            final String initKey = toInit(authorization.getId());

            if (Boolean.TRUE.equals(this.redisTemplate.hasKey(initKey))) {
                redisTemplate.delete(initKey);
            }
        } else {
            key = toInit(authorization.getId());
        }

        log.info("saved user's authorization object with id {}", authorization.getId());

        this.authorizations.set(key, authorization, oAuth2Options.getRedis().getTtl(), TimeUnit.HOURS);
    }

    /**
     * Удаление
     */
    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");

        final String key = isComplete(authorization)
                ? toComplete(authorization.getId())
                : toInit(authorization.getId());

        log.debug("delete authorization object with key {}", key);

        this.redisTemplate.delete(key);
    }

    /**
     * Поиск по идентификатору авторизации
     */
    @Nullable
    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");

        final var oAuth2Authorization = this.findById(id, oAuth2Options.getRedis().getCompletePrefix());

        return Optional.ofNullable(oAuth2Authorization)
                .orElseGet(() -> this.findById(id, oAuth2Options.getRedis().getInitPrefix()));
    }

    private OAuth2Authorization findById(final String id, final String prefix) {
        final var finalId = prefix + id;

        log.info("find authorization object with key {}", finalId);

        OAuth2Authorization authorization = this.authorizations.get(finalId);

        if (Objects.isNull(authorization)) {
            log.info("not found authorization object with key {}", finalId);
        } else {
            log.info("found authorization object with key {}", finalId);
        }

        return authorization;
    }

    @Nullable
    @Override
    public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");

        final var oAuth2Authorization = this.findByToken(token, tokenType, oAuth2Options.getRedis().getCompletePrefix());

        return Optional.ofNullable(oAuth2Authorization)
                .orElseGet(() -> this.findByToken(token, tokenType, oAuth2Options.getRedis().getInitPrefix()));
    }

    private OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType, final String prefixKey) {
        final var keys = redisTemplate.keys(prefixKey + "*");

        final var initKeys = Optional.ofNullable(keys).orElse(Set.of());

        return initKeys.stream()
                .map(this.authorizations::get)
                .filter(oAuth2Authorization -> hasToken(oAuth2Authorization, token, tokenType))
                .findFirst()
                .orElse(null);
    }

    private String toComplete(final String id) {
        return oAuth2Options.getRedis().getCompletePrefix() + id;
    }

    private String toInit(final String id) {
        return oAuth2Options.getRedis().getInitPrefix() + id;
    }

}
