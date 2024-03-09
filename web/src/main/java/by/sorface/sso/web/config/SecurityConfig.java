package by.sorface.sso.web.config;

import by.sorface.sso.web.config.handlers.SavedRequestRedisSuccessHandler;
import by.sorface.sso.web.config.handlers.TokenAuthenticationSuccessHandler;
import by.sorface.sso.web.config.properties.MvcEndpointProperties;
import by.sorface.sso.web.constants.UrlPatternEnum;
import by.sorface.sso.web.dao.repository.OAuth2ClientRepository;
import by.sorface.sso.web.services.providers.OAuth2UserDatabaseProvider;
import by.sorface.sso.web.services.providers.SfUserDatabaseProvider;
import by.sorface.sso.web.services.redis.RedisOAuth2AuthorizationConsentService;
import by.sorface.sso.web.services.redis.RedisOAuth2AuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    private final SavedRequestRedisSuccessHandler savedRequestRedisSuccessHandler;

    private final RedisOAuth2AuthorizationService redisOAuth2AuthorizationService;

    private final RedisOAuth2AuthorizationConsentService redisOAuth2AuthorizationConsentService;

    private final OAuth2UserDatabaseProvider oAuth2UserDatabaseProvider;

    private final TokenAuthenticationSuccessHandler tokenAuthenticationSuccessHandler;

    private final MvcEndpointProperties mvcEndpointProperties;

    private final SfUserDatabaseProvider userDetailsService;

    private final PasswordEncoder passwordEncoder;

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
        {
            authorizationServerConfigurer.setBuilder(httpSecurity);

            authorizationServerConfigurer.tokenIntrospectionEndpoint(configurer ->
                    configurer.introspectionResponseHandler(tokenAuthenticationSuccessHandler));

            authorizationServerConfigurer.authorizationService(redisOAuth2AuthorizationService);
            authorizationServerConfigurer.authorizationConsentService(redisOAuth2AuthorizationConsentService);
        }

        final RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        httpSecurity
                .securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(configure -> {
                    configure.requestMatchers(UrlPatternEnum.toArray()).permitAll();
                    configure.anyRequest().authenticated();
                })
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .exceptionHandling(configurer -> {
                    final var loginUrlAuthenticationEntryPoint =
                            new LoginUrlAuthenticationEntryPoint(mvcEndpointProperties.getUriPageSignIn());

                    configurer.authenticationEntryPoint(loginUrlAuthenticationEntryPoint);
                })
                .apply(authorizationServerConfigurer);

        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(final HttpSecurity httpSecurity,
                                                          OAuth2ClientRepository oAuth2ClientRepository) throws Exception {
        httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        httpSecurity.oauth2Login(configurer -> {
            configurer.userInfoEndpoint(configure -> configure.userService(oAuth2UserDatabaseProvider));

            configurer.loginPage(mvcEndpointProperties.getUriPageSignIn());
        });

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(UrlPatternEnum.toArray()).permitAll()
                                .anyRequest().authenticated()
                )
                .logout(configurer -> {
                    configurer.clearAuthentication(true);
                    configurer.invalidateHttpSession(true);

                    configurer.logoutUrl(mvcEndpointProperties.getUriApiLogout());
                    configurer.deleteCookies("SESSION", "JSESSION");
                })
                .formLogin(configurer -> {
                    configurer.loginPage(mvcEndpointProperties.getUriPageSignIn());
                    configurer.loginProcessingUrl(mvcEndpointProperties.getUriApiLogin());

                    configurer.successHandler(savedRequestRedisSuccessHandler);
                })
                .build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

}
