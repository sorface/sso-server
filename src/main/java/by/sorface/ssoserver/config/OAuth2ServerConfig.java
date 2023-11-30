package by.sorface.ssoserver.config;

import by.sorface.ssoserver.config.handlers.IntrospectionHttpWriter;
import by.sorface.ssoserver.constants.enums.UrlPatterns;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class OAuth2ServerConfig {

    private final IntrospectionHttpWriter introspectionHttpWriter;

    /**
     * Configuration OAuth2 Spring Security
     *
     * @param http configuring web based security
     * @return security chain
     * @throws Exception исключение ошибки настройки
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(final HttpSecurity http) throws Exception {
        final OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        authorizationServerConfigurer
                .tokenIntrospectionEndpoint(tokenIntrospectionEndpointConfigurer ->
                        tokenIntrospectionEndpointConfigurer.introspectionResponseHandler(introspectionHttpWriter::write));

        final RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        http
                .securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(this::configureAuthorizeHttRequest)
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .exceptionHandling(this::configureExceptionHandling)
                .apply(authorizationServerConfigurer);

        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * configuration http matcher registry
     *
     * @param authorizationManagerRequestMatcherRegistry config object
     */
    private void configureAuthorizeHttRequest(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationManagerRequestMatcherRegistry) {
        authorizationManagerRequestMatcherRegistry
                .requestMatchers(UrlPatterns.toArray()).permitAll()
                .anyRequest().authenticated();
    }

    /**
     * configuration exception handling
     *
     * @param exceptionHandlingConfigurer config object
     */
    private void configureExceptionHandling(ExceptionHandlingConfigurer<HttpSecurity> exceptionHandlingConfigurer) {
        final var authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint("/login");

        exceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint);
    }

}
