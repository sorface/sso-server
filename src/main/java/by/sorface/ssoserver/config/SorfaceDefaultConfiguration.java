package by.sorface.ssoserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SorfaceDefaultConfiguration {

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
    public OAuth2AuthorizationService oAuth2AuthorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }

    /**
     * Создание компонента кодирования паролей
     *
     * @return компонент кодирования паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
