package by.sorface.sso.web.config.security;

import by.sorface.sso.web.config.options.CookieOptions;
import by.sorface.sso.web.config.options.EndpointOptions;
import by.sorface.sso.web.config.redis.RedisSessionLogoutHandler;
import by.sorface.sso.web.config.security.csrf.CsrfCookieFilter;
import by.sorface.sso.web.config.security.csrf.SpaCsrfTokenRequestHandler;
import by.sorface.sso.web.constants.UrlPatternEnum;
import by.sorface.sso.web.services.providers.OAuth2UserDatabaseStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@EnableGlobalAuthentication
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(final HttpSecurity httpSecurity,
                                                          final SavedRequestRedisSuccessHandler savedRequestRedisSuccessHandler,
                                                          final AuthenticationFailureHandler authenticationFailureHandler,
                                                          final OAuth2UserDatabaseStrategy oAuth2UserDatabaseStrategy,
                                                          final RedisSessionLogoutHandler redisSessionLogoutHandler,
                                                          final ApiAuthenticationEntryPoint apiAuthenticationEntryPoint,
                                                          final CookieCsrfTokenRepository csrfTokenRepository,
                                                          final CookieOptions cookieOptions,
                                                          final EndpointOptions endpointOptions) throws Exception {
        httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        httpSecurity.oauth2Login(
                oAuth2LoginConfigurerCustomizer(oAuth2UserDatabaseStrategy, endpointOptions, savedRequestRedisSuccessHandler, authenticationFailureHandler)
        );

        return httpSecurity
                .csrf(csrfConfigurerCustomizer(csrfTokenRepository))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistryCustomizer())
                .logout(logoutConfigurerCustomizer(redisSessionLogoutHandler, endpointOptions, cookieOptions))
                .formLogin(formLoginConfigurerCustomizer(endpointOptions, savedRequestRedisSuccessHandler, authenticationFailureHandler))
                .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer.authenticationEntryPoint(apiAuthenticationEntryPoint))
                .build();
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

    private Customizer<OAuth2LoginConfigurer<HttpSecurity>> oAuth2LoginConfigurerCustomizer(final OAuth2UserDatabaseStrategy oAuth2UserDatabaseStrategy,
                                                                                            final EndpointOptions endpointOptions,
                                                                                            final AuthenticationSuccessHandler authenticationSuccessHandler,
                                                                                            final AuthenticationFailureHandler authenticationFailureHandler) {
        return configurer -> {
            configurer.userInfoEndpoint(configure -> configure.userService(oAuth2UserDatabaseStrategy));

            configurer.loginPage(endpointOptions.getUriPageSignIn());

            configurer.successHandler(authenticationSuccessHandler);
            configurer.failureHandler(authenticationFailureHandler);
        };
    }

    /**
     * Configuration formLogin settings
     *
     * @return customizer for formLogin settings
     */
    private Customizer<FormLoginConfigurer<HttpSecurity>> formLoginConfigurerCustomizer(final EndpointOptions endpointOptions,
                                                                                        final AuthenticationSuccessHandler authenticationSuccessHandler,
                                                                                        final AuthenticationFailureHandler authenticationFailureHandler) {
        return configurer -> {
            configurer.loginPage(endpointOptions.getUriPageSignIn());
            configurer.loginProcessingUrl(endpointOptions.getUriApiLogin());

            configurer.successHandler(authenticationSuccessHandler);
            configurer.failureHandler(authenticationFailureHandler);
        };
    }

    /**
     * Configuration csrf security
     *
     * @return customizer for csrf security
     */
    private Customizer<CsrfConfigurer<HttpSecurity>> csrfConfigurerCustomizer(final CsrfTokenRepository csrfTokenRepository) {
        return csrfConfigurer -> {
            csrfConfigurer.ignoringRequestMatchers(UrlPatternEnum.toArray(UrlPatternEnum.API_ACCOUNT));
            csrfConfigurer.csrfTokenRepository(csrfTokenRepository);
            csrfConfigurer.csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler());
            csrfConfigurer.sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());
        };
    }

    /**
     * Configuration request access for app
     *
     * @return customizer for request access for app
     */
    private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizationManagerRequestMatcherRegistryCustomizer() {
        return configure -> configure
                .requestMatchers(HttpMethod.OPTIONS, UrlPatternEnum.toArray(UrlPatternEnum.OPTION_REQUEST)).permitAll()
                .requestMatchers(HttpMethod.GET, UrlPatternEnum.toArray(UrlPatternEnum.API_ACCOUNT)).permitAll()
                .requestMatchers(UrlPatternEnum.toArray()).permitAll()
                .anyRequest()
                .authenticated();
    }

    /**
     * Configuration logout settings
     *
     * @return customizer with logout settings
     */
    private Customizer<LogoutConfigurer<HttpSecurity>> logoutConfigurerCustomizer(final LogoutHandler logoutHandler,
                                                                                  final EndpointOptions endpointOptions,
                                                                                  final CookieOptions cookieOptions) {
        return configurer -> {
            configurer.invalidateHttpSession(true);
            configurer.clearAuthentication(true);

            configurer.addLogoutHandler(logoutHandler);

            configurer.logoutUrl(endpointOptions.getUriApiLogout());
            configurer.deleteCookies(cookieOptions.getSession().getName(), cookieOptions.getCsrf().getName());
        };
    }

}
