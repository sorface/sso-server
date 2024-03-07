package by.sorface.sso.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis.sorface.auth")
public class RedisOAuth2Properties {

    private String completePrefix;

    private String initPrefix;

}
