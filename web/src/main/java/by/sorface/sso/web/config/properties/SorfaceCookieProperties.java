package by.sorface.sso.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(value = "spring.security.sorface.cookie")
public class SorfaceCookieProperties {

    private String domainPattern;

    private String path;

    private String name;

}
