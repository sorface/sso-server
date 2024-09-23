package by.sorface.sso.web.config.options;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Data
@Configuration
@ConfigurationProperties("sorface.oauth2")
public class OAuth2Options {

    private String issuerUrl;

    private RedisOptions redis;

    @Data
    public static class RedisOptions {

        private RedisDescriptionOptions init;

        private RedisDescriptionOptions complete;

        private RedisDescriptionOptions consent;

    }

    @Data
    public static class RedisDescriptionOptions {

        private String prefix;

        private long ttl;

        private TimeUnit unit;

    }

}
