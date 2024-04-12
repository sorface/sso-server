package by.sorface.sso.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties("sorface.cors")
public class CorsOptions {

    private List<CorsItemOptions> options;

    @Data
    public static class CorsItemOptions {

        private String pattern;

        private String allowedOrigins;

        private String allowedOriginPatterns;

        private String allowedHeaders;

        private String exposedHeaders;

        private String allowedMethods;

        private boolean allowCredentials;

        private Long maxAge;
    }

}
