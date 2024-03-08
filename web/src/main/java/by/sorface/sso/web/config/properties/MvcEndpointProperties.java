package by.sorface.sso.web.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.mvc.endpoint")
public class MvcEndpointProperties {

    private String uriPageLogin;

    private String uriPageProfile;

    private String uriApiLogin;

    private String uriApiLogout;

}
