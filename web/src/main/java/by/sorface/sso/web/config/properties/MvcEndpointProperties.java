package by.sorface.sso.web.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.mvc.endpoint")
public class MvcEndpointProperties {

    /**
     * uri signin page
     */
    private String uriPageSignIn;

    /**
     * uri signup page
     */
    private String uriPageSignUp;

    /**
     * uri profile page
     */
    private String uriPageProfile;

    /**
     * uri not found page
     */
    private String uriPageNotFound;

    /**
     * uri api signin
     */
    private String uriApiLogin;

    /**
     * uri api logout
     */
    private String uriApiLogout;

}
