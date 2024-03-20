package by.sorface.sso.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(value = "spring.security.sorface.sso")
public class SorfaceSsoProperties {

    private String issuerUrl;

}
