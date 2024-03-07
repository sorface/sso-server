package by.sorface.sso.web.config;

import by.sorface.sso.web.config.properties.RedisOAuth2Properties;
import by.sorface.sso.web.services.redis.RedisOAuth2AuthorizationConsentService;
import by.sorface.sso.web.services.redis.RedisOAuth2AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

@RequiredArgsConstructor
@EnableRedisRepositories
@Configuration(proxyBeanMethods = false)
public class OAuth2RedisConfiguration {

    @Bean
    public RedisTemplate<String, OAuth2Authorization> redisTemplateOAuth2Authorization(final RedisConnectionFactory redisConnectionFactory) {
        final var redisTemplate = new RedisTemplate<String, OAuth2Authorization>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplateOAuth2AuthorizationConsent(final RedisConnectionFactory redisConnectionFactory) {
        final var redisTemplate = new RedisTemplate<String, OAuth2AuthorizationConsent>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService(final RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplate) {
        return new RedisOAuth2AuthorizationConsentService(redisTemplate, 720000);
    }

    @Bean
    public OAuth2AuthorizationService oAuth2AuthorizationService(final RedisOAuth2Properties redisOAuth2Properties,
                                                                 final RedisTemplate<String, OAuth2Authorization> redisTemplate) {
        return new RedisOAuth2AuthorizationService(redisTemplate, redisOAuth2Properties, 720000);
    }

}
