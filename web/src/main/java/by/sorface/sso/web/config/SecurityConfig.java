package by.sorface.sso.web.config;

import by.sorface.sso.web.config.handlers.SavedRequestRedisSuccessHandler;
import by.sorface.sso.web.config.handlers.TokenAuthenticationSuccessHandler;
import by.sorface.sso.web.config.properties.MvcEndpointProperties;
import by.sorface.sso.web.config.properties.SorfaceCookieProperties;
import by.sorface.sso.web.constants.FrontendUrlPattern;
import by.sorface.sso.web.constants.UrlPatternEnum;
import by.sorface.sso.web.records.responses.OperationError;
import by.sorface.sso.web.services.providers.OAuth2UserDatabaseStrategy;
import by.sorface.sso.web.services.redis.RedisOAuth2AuthorizationConsentService;
import by.sorface.sso.web.services.redis.RedisOAuth2AuthorizationService;
import by.sorface.sso.web.utils.json.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    private final SavedRequestRedisSuccessHandler savedRequestRedisSuccessHandler;

    private final RedisOAuth2AuthorizationService redisOAuth2AuthorizationService;

    private final RedisOAuth2AuthorizationConsentService redisOAuth2AuthorizationConsentService;

    private final OAuth2UserDatabaseStrategy oAuth2UserDatabaseStrategy;

    private final TokenAuthenticationSuccessHandler tokenAuthenticationSuccessHandler;

    private final MvcEndpointProperties mvcEndpointProperties;

    private final SorfaceCookieProperties sorfaceCookieProperties;

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
                    configure.requestMatchers(
                                    FrontendUrlPattern.PAGE_PROFILE.getEndpoint(),
                                    FrontendUrlPattern.PAGE_SESSIONS.getEndpoint()
                            )
                            .authenticated();
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
    public SecurityFilterChain defaultSecurityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        httpSecurity.oauth2Login(configurer -> {
            configurer.userInfoEndpoint(configure -> configure.userService(oAuth2UserDatabaseStrategy));

            configurer.loginPage(mvcEndpointProperties.getUriPageSignIn());

            configurer.successHandler(savedRequestRedisSuccessHandler);
            configurer.failureHandler((request, response, exception) -> {
                OperationError operationError = new OperationError("ERROR", exception.getMessage(), 400);
                response.sendRedirect("forward:" + mvcEndpointProperties.getUriPageSignIn() + "?error=" + Json.stringify(operationError));
            });
        });

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(configure -> {
                    configure.requestMatchers(
                                    FrontendUrlPattern.PAGE_PROFILE.getEndpoint(),
                                    FrontendUrlPattern.PAGE_SESSIONS.getEndpoint()
                            )
                            .authenticated();
                    configure
                            .requestMatchers(UrlPatternEnum.toArray()).permitAll()
                            .anyRequest().authenticated();
                })
                .logout(configurer -> {
                    configurer.clearAuthentication(true);
                    configurer.invalidateHttpSession(true);

                    configurer.logoutUrl(mvcEndpointProperties.getUriApiLogout());
                    configurer.deleteCookies(sorfaceCookieProperties.getName());
                })
                .formLogin(configurer -> {
                    configurer.loginPage(mvcEndpointProperties.getUriPageSignIn());
                    configurer.loginProcessingUrl(mvcEndpointProperties.getUriApiLogin());

                    configurer.successHandler(savedRequestRedisSuccessHandler);
                    configurer.failureHandler((request, response, exception) -> {
                        String error;
                        if (exception instanceof BadCredentialsException) {
                            error = "password.or.username.invalid";
                        } else {
                            error = "unknown.error";
                        }

                        final var encode = UriUtils.encode(error, StandardCharsets.US_ASCII);
                        final var source = mvcEndpointProperties.getUriPageSignIn() + "?error=" + encode;

                        response.sendRedirect(source);
                    });
                })
                .build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

}
