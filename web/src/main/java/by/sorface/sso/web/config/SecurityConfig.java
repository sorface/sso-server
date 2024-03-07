package by.sorface.sso.web.config;

import by.sorface.sso.web.config.handlers.IntrospectionTokenPrincipalHandler;
import by.sorface.sso.web.config.properties.MvcLoginProperties;
import by.sorface.sso.web.constants.UrlPatternEnum;
import by.sorface.sso.web.services.providers.OAuth2UserDatabaseProvider;
import by.sorface.sso.web.services.providers.SorfaceUserDatabaseProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    private final OAuth2UserDatabaseProvider oAuth2UserDatabaseProvider;

    private final IntrospectionTokenPrincipalHandler introspectionTokenPrincipalHandler;

    private final MvcLoginProperties mvcLoginProperties;

    /**
     * Configuration OAuth2 Spring Security
     *
     * @param httpSecurity configuring web based security
     * @return security chain
     * @throws Exception исключение ошибки настройки
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        final var authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        authorizationServerConfigurer.tokenIntrospectionEndpoint(tokenIntrospectionEndpointConfigurer -> {
            tokenIntrospectionEndpointConfigurer.introspectionResponseHandler(introspectionTokenPrincipalHandler::write);
        });

        final RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        httpSecurity
                .securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(UrlPatternEnum.toArray()).permitAll()
                                .anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
                .apply(authorizationServerConfigurer);

        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        httpSecurity.oauth2Login(configurer -> {
            configurer.userInfoEndpoint(configure -> configure.userService(oAuth2UserDatabaseProvider));

            configurer.successHandler(new SimpleUrlAuthenticationSuccessHandler(
                    "/home"
            ));
            configurer.failureHandler(new SimpleUrlAuthenticationFailureHandler());

            configurer.loginPage(mvcLoginProperties.getPageEndpoint());
        });

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(UrlPatternEnum.toArray()).permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(configurer -> {
                    configurer.loginPage(mvcLoginProperties.getPageEndpoint());
                    configurer.loginProcessingUrl(mvcLoginProperties.getApiEndpoint());

                    configurer.successHandler((request, response, authentication) ->
                            response.setHeader("Sorface-Next-Location", "http://localhost:8080/home"));
                    configurer.failureHandler(new SimpleUrlAuthenticationFailureHandler("/error"));
                })
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(final SorfaceUserDatabaseProvider userDetailsService,
                                                       final PasswordEncoder passwordEncoder) {
        final var authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

}
