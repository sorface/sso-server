package by.sorface.sso.web.services.redis;

import by.sorface.sso.web.config.options.OAuth2Options;
import by.sorface.sso.web.dao.models.OAuth2Client;
import by.sorface.sso.web.records.principals.DefaultPrincipal;
import by.sorface.sso.web.records.principals.OAuth2Session;
import by.sorface.sso.web.services.clients.OAuth2ClientService;
import by.sorface.sso.web.utils.OAuth2AuthorizationUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public final class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final static String PRINCIPAL_ATTRIBUTE_KEY = "java.security.Principal";

    private final RedisTemplate<String, OAuth2Authorization> redisTemplate;

    private final ValueOperations<String, OAuth2Authorization> authorizations;

    private final OAuth2Options oAuth2Options;

    private final RedisTemplate<String, OAuth2Session> redisTemplateOAuth2Session;

    private final ValueOperations<String, OAuth2Session> oauth2Session;

    private final OAuth2ClientService clientService;

    public RedisOAuth2AuthorizationService(final RedisTemplate<String, OAuth2Authorization> redisTemplate,
                                           final OAuth2Options oAuth2Options,
                                           final RedisTemplate<String, OAuth2Session> redisTemplateOAuth2Session,
                                           OAuth2ClientService clientService) {
        this.redisTemplate = redisTemplate;
        this.authorizations = redisTemplate.opsForValue();
        this.oAuth2Options = oAuth2Options;
        this.redisTemplateOAuth2Session = redisTemplateOAuth2Session;
        this.oauth2Session = redisTemplateOAuth2Session.opsForValue();
        this.clientService = clientService;
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

    private static boolean matchesState(final OAuth2Authorization authorization, final String token) {
        return token.equals(authorization.getAttribute(OAuth2ParameterNames.STATE));
    }

    private static boolean matchesAuthorizationCode(OAuth2Authorization authorization, String token) {
        final OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        return authorizationCode != null && authorizationCode.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesAccessToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);
        return accessToken != null && accessToken.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesRefreshToken(OAuth2Authorization authorization, String token) {
        final OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getToken(OAuth2RefreshToken.class);
        return refreshToken != null && refreshToken.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesIdToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OidcIdToken> idToken = authorization.getToken(OidcIdToken.class);
        return idToken != null && idToken.getToken().getTokenValue().equals(token);
    }

    @Override
    public void save(final OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");

        log.info("save user's authorization object with id {}", authorization.getId());

        String key;

        if (isComplete(authorization)) {
            key = toComplete(authorization.getId());

            this.saveInformation(authorization);

            final String initKey = toInit(authorization.getId());

            if (Boolean.TRUE.equals(this.redisTemplate.hasKey(initKey))) {
                redisTemplate.delete(initKey);
            }
        } else {
            key = toInit(authorization.getId());
        }

        log.info("saved user's authorization object with id {}", authorization.getId());

        this.authorizations.set(key, authorization, oAuth2Options.getRedis().getComplete().getTtl(), oAuth2Options.getRedis().getComplete().getUnit());
    }

    @Override
    public void remove(final OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");

        final String key = isComplete(authorization)
                ? toComplete(authorization.getId())
                : toInit(authorization.getId());

        log.debug("delete authorization object with key {}", key);

        this.redisTemplate.delete(key);
    }

    @Nullable
    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");

        final var oAuth2Authorization = this.findById(id, oAuth2Options.getRedis().getComplete());

        return Optional.ofNullable(oAuth2Authorization)
                .orElseGet(() -> this.findById(id, oAuth2Options.getRedis().getInit()));
    }

    public void saveInformation(final OAuth2Authorization authorization) {
        final PrincipalInformation principalInformation = getPrincipalInformation(authorization);

        if (Objects.isNull(principalInformation.getPrincipalId())) {
            return;
        }

        final String key = toInfo(principalInformation.getPrincipalId());

        final OAuth2Client client = clientService.findById(UUID.fromString(authorization.getRegisteredClientId()));

        final var oAuth2Session = OAuth2Session.builder()
                .authorizationId(authorization.getId())
                .principleId(principalInformation.getPrincipalId())
                .initiatorSystem(client.getClientName())
                .build();

        oauth2Session.set(key, oAuth2Session, oAuth2Options.getRedis().getInfo().getTtl(), oAuth2Options.getRedis().getInit().getUnit());
    }

    private PrincipalInformation getPrincipalInformation(final OAuth2Authorization oAuth2Authorization) {
        final DefaultPrincipal principal = OAuth2AuthorizationUtils.getPrincipal(oAuth2Authorization);

        if (Objects.isNull(principal)) {
            return PrincipalInformation.builder().build();
        }

        return PrincipalInformation.builder()
                .principalId(principal.getId())
                .build();
    }

    public void remove(final UUID principalId) {
        final String keyInfo = toInfo(principalId);

        final Boolean hasKey = redisTemplateOAuth2Session.hasKey(keyInfo);

        if (Objects.isNull(hasKey) || Boolean.FALSE.equals(hasKey)) {
            return;
        }

        final OAuth2Session oAuth2Session = oauth2Session.get(keyInfo);

        if (Objects.isNull(oAuth2Session)) {
            return;
        }

        final String authorizationId = oAuth2Session.getAuthorizationId();

        if (Objects.isNull(authorizationId) || authorizationId.trim().isEmpty()) {
            return;
        }

        final String completeKey = toComplete(authorizationId);

        Boolean hasCompleteKey = redisTemplate.hasKey(completeKey);

        if (Objects.isNull(hasCompleteKey) || Boolean.FALSE.equals(hasCompleteKey)) {
            return;
        }

        redisTemplate.delete(List.of(completeKey, keyInfo));
    }

    private OAuth2Authorization findById(final String id, final OAuth2Options.RedisDescriptionOptions redisDescriptionOptions) {
        final var finalId = redisDescriptionOptions.getPrefix() + id;

        log.info("find authorization object with key {}", finalId);

        OAuth2Authorization authorization = this.authorizations.getAndExpire(finalId, redisDescriptionOptions.getTtl(), redisDescriptionOptions.getUnit());

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

        final var oAuth2Authorization = this.findByToken(token, tokenType, oAuth2Options.getRedis().getComplete());

        if (Objects.nonNull(oAuth2Authorization)) {
            updateExpiration(oAuth2Authorization);
        }

        return Optional.ofNullable(oAuth2Authorization)
                .orElseGet(() -> this.findByToken(token, tokenType, oAuth2Options.getRedis().getInit()));
    }

    private OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType, final OAuth2Options.RedisDescriptionOptions redisDescriptionOptions) {
        final var keys = redisTemplate.keys(redisDescriptionOptions.getPrefix() + "*");

        final var optionKeys = Optional.ofNullable(keys).orElse(Set.of());

        return optionKeys.stream()
                .map(this.authorizations::get)
                .filter(authorization -> hasToken(authorization, token, tokenType))
                .findFirst()
                .orElse(null);
    }

    private String toComplete(final String id) {
        return oAuth2Options.getRedis().getComplete().getPrefix() + id;
    }

    private String toInit(final String id) {
        return oAuth2Options.getRedis().getInit().getPrefix() + id;
    }

    private String toInfo(final UUID principalId) {
        return oAuth2Options.getRedis().getInfo().getPrefix() + "_" + principalId;
    }

    private void updateExpiration(final OAuth2Authorization authorization) {
        if (!(isNeedUpdateExpiration(oAuth2Options.getRedis().getComplete()) && isNeedUpdateExpiration(oAuth2Options.getRedis().getInfo()))) {
            return;
        }

        final PrincipalInformation principalInformation = getPrincipalInformation(authorization);

        final String principalKey = toInfo(principalInformation.getPrincipalId());

        final Boolean hasKey = redisTemplateOAuth2Session.hasKey(principalKey);

        if (Objects.isNull(hasKey) || Boolean.FALSE.equals(hasKey)) {
            return;
        }

        String completeKey = toComplete(authorization.getId());

        final Boolean hasComplete = redisTemplate.hasKey(completeKey);

        if (Objects.isNull(hasComplete) || Boolean.FALSE.equals(hasComplete)) {
            return;
        }

        redisTemplateOAuth2Session.expire(principalKey, oAuth2Options.getRedis().getInfo().getTtl(), oAuth2Options.getRedis().getInfo().getUnit());
        redisTemplate.expire(completeKey, oAuth2Options.getRedis().getComplete().getTtl(), oAuth2Options.getRedis().getComplete().getUnit());
    }

    private boolean isNeedUpdateExpiration(OAuth2Options.RedisDescriptionOptions redisDescriptionOptions) {
        final Instant expireTime = Instant.now().minus(redisDescriptionOptions.getTtl(), redisDescriptionOptions.getUnit().toChronoUnit());

        final var now = Instant.now();
        final var criticalBoundTime = now.minus(30, ChronoUnit.SECONDS);

        return expireTime.isAfter(criticalBoundTime) && expireTime.isBefore(now);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class PrincipalInformation {

        private UUID principalId;

        private String authorizationId;

    }

}
