package by.sorface.sso.web.config;

import by.sorface.sso.web.config.properties.SorfaceCookieProperties;
import lombok.RequiredArgsConstructor;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableGlobalAuthentication
@RequiredArgsConstructor
public class SorfaceConfiguration {

    private final SorfaceCookieProperties sorfaceCookieProperties;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final var configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));

        final var source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        final var serializer = new DefaultCookieSerializer();
        {
            serializer.setCookieName(sorfaceCookieProperties.getName());
            serializer.setCookiePath(sorfaceCookieProperties.getPath());
            serializer.setDomainNamePattern(sorfaceCookieProperties.getDomainPattern());
        }

        return serializer;
    }

    /**
     * Creating a password encoding component
     *
     * @return the password encoding component
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public ResourceBundleMessageSource resourceBundleMessageSource() {
        final var source = new ResourceBundleMessageSource();
        {
            source.setBasename("language/messages");
            source.setDefaultEncoding("UTF-8");
        }

        return source;
    }

    @Bean
    public UserAgentAnalyzer userAgentAnalyzer() {
        return UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(10000).build();
    }

}
