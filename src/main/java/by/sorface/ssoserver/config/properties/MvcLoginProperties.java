package by.sorface.ssoserver.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.mvc.login")
public class MvcLoginProperties {

    private String pageEndpoint;

    private String apiEndpoint;

}
