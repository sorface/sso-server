package by.sorface.ssoserver.config;

import by.sorface.ssoserver.constants.enums.UrlPatterns;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

    /**
     * Конфигурация OAuth2 Spring Security
     *
     * @param http configuring web based security
     * @return security chain
     * @throws Exception исключение ошибки настройки
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(final HttpSecurity http) throws Exception {
        final var authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        final RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        http
                .securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(UrlPatterns.toArray()).permitAll()
                            .anyRequest().authenticated();
                })
                .exceptionHandling(exceptions -> {
                    final var authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint("/login");
                    exceptions.authenticationEntryPoint(authenticationEntryPoint);
                })
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .apply(authorizationServerConfigurer);

        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(final AuthorizationServerProperties authorizationServerProperties) {
        return AuthorizationServerSettings.builder()
                .issuer(authorizationServerProperties.getIssuerUrl())
                .tokenIntrospectionEndpoint(authorizationServerProperties.getIntrospectionEndpoint())
                .build();
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
