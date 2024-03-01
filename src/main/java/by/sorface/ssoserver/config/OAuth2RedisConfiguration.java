package by.sorface.ssoserver.config;

import by.sorface.ssoserver.services.redis.RedisOAuth2AuthorizationConsentService;
import by.sorface.ssoserver.services.redis.RedisOAuth2AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class OAuth2RedisConfiguration {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, OAuth2Authorization> redisTemplateOAuth2Authorization(
            RedisConnectionFactory redisConnectionFactory
    ) {
        RedisTemplate<String, OAuth2Authorization> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplateOAuth2AuthorizationConsent(
            RedisConnectionFactory redisConnectionFactory
    ) {
        RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService(
            RedisTemplate<String, OAuth2AuthorizationConsent> redisTemplate
    ) {
        return new RedisOAuth2AuthorizationConsentService(redisTemplate, 720000);
    }

    @Bean
    public OAuth2AuthorizationService oAuth2AuthorizationService(
            RedisTemplate<String, OAuth2Authorization> redisTemplate
    ) {
        return new RedisOAuth2AuthorizationService(redisTemplate, 720000);
    }

}
