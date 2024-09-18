package by.sorface.sso.web.services.redis;

import by.sorface.sso.web.config.options.OAuth2Options;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public final class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private final RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplate;

    private final ValueOperations<String, OAuth2AuthorizationConsent> authorizationConsents;

    private final OAuth2Options oAuth2Options;

    public RedisOAuth2AuthorizationConsentService(final RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplate,
                                                  final OAuth2Options oAuth2Options) {
        this.redisTemplate = redisTemplate;
        this.authorizationConsents = redisTemplate.opsForValue();
        this.oAuth2Options = oAuth2Options;
    }

    private static String getId(final String registeredClientId, final String principalName) {
        return String.valueOf(Objects.hash(registeredClientId, principalName));
    }

    private static String getId(final OAuth2AuthorizationConsent authorizationConsent) {
        return getId(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        String id = getId(authorizationConsent);

        this.authorizationConsents.set(buildKey(id), authorizationConsent, oAuth2Options.getRedis().getConsent().getTtl(), oAuth2Options.getRedis().getConsent().getUnit());
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        String id = getId(authorizationConsent);

        this.redisTemplate.delete(buildKey(id));
    }

    @Override
    @Nullable
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        String id = getId(registeredClientId, principalName);

        final long ttl = oAuth2Options.getRedis().getConsent().getTtl();
        final TimeUnit unit = oAuth2Options.getRedis().getConsent().getUnit();

        return this.authorizationConsents.getAndExpire(id, ttl, unit);
    }

    private String buildKey(final String id) {
        return oAuth2Options.getRedis().getConsent().getPrefix() + id;
    }
}
