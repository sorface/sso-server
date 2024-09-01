package by.sorface.sso.web.config.options.locale;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("sorface.locale.cookie")
public class LocaleCookieOptions {

    private String name;

    private String domain;

    private String path;

    private boolean httpOnly = false;

}
