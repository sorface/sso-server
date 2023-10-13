package by.sorface.ssoserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "sorface.sso.token")
public class SorfaceTokenProperties {

    private String format;

    private Integer accessTimeLive;

    private Integer refreshTimeLive;

    private boolean reuseRefreshToken;

    private Integer authorizationCodeTimeLive;

}
