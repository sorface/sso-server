package by.sorface.sso.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("sorface.oauth2")
public class OAuth2Options {

    private String issuerUrl;

    private RedisOptions redis;

    @Data
    public static class RedisOptions {

        private String completePrefix;

        private String initPrefix;

        private long ttl;

    }
}
