package by.sorface.sso.web.config.options;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("sorface.cookie")
public class CookieOptions {

    private SessionCookieOptions session;

    private CsrfCookieOptions csrf;

    @Data
    public static class SessionCookieOptions {

        private String domainPattern;

        private String path;

        private String name;

        private boolean httpOnly = false;

    }

    @Data
    public static class CsrfCookieOptions {

        private String domain;

        private String path;

        private String name;

        private boolean httpOnly = false;

    }
}
