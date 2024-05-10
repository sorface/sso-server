package by.sorface.sso.web.config.security;

import by.sorface.sso.web.config.options.CookieOptions;
import by.sorface.sso.web.config.options.EndpointOptions;
import by.sorface.sso.web.config.security.csrf.CsrfCookieFilter;
import by.sorface.sso.web.config.security.handlers.SavedRequestRedisSuccessHandler;
import by.sorface.sso.web.config.security.redis.RedisSessionLogoutHandler;
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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@EnableGlobalAuthentication
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    /**
     * The defaultSecurityFilterChain function is a Spring Security configuration function that configures the
     * default security filter chain. The default security filter chain is used to protect all endpoints except those
     * explicitly excluded by other functions in this class. This function configures the following:
     * <p>
     * &lt;ul&gt;
     * &lt;li&gt;OAuth2 login&lt;/li&gt;
     * &lt;li&gt;CSRF protection&lt;/li&gt;
     * &lt;li&gt;&lt;a href=&quot;http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#authorize-requests&quot;&gt;Authorization manager request matcher registry customizer&lt;/
     *
     * @param httpSecurity                    Access the authenticationmanagerbuilder
     * @param savedRequestRedisSuccessHandler Inject the bean into this method
     * @param authenticationFailureHandler    Pass in the authenticationfailurehandler bean
     * @param oAuth2UserDatabaseStrategy      Inject the oauth2userdatabasestrategy bean into this function
     * @param redisSessionLogoutHandler       Logout the user from all sessions
     * @param apiAuthenticationEntryPoint     Configure the exception handling
     * @param cookieOptions                   Set the cookie options for the session
     * @param endpointOptions                 Configure the endpoint url for oauth2 login
     * @return The securityfilterchain
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(final HttpSecurity httpSecurity,
                                                          final SavedRequestRedisSuccessHandler savedRequestRedisSuccessHandler,
                                                          final AuthenticationFailureHandler authenticationFailureHandler,
                                                          final OAuth2UserDatabaseStrategy oAuth2UserDatabaseStrategy,
                                                          final RedisSessionLogoutHandler redisSessionLogoutHandler,
                                                          final ApiAuthenticationEntryPoint apiAuthenticationEntryPoint,
                                                          final CookieCsrfTokenRepository cookieCsrfTokenRepository,
                                                          final CsrfTokenRequestHandler csrfTokenRequestHandler,
                                                          final CookieOptions cookieOptions,
                                                          final EndpointOptions endpointOptions) throws Exception {
        httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        httpSecurity.oauth2Login(
                oAuth2LoginConfigurerCustomizer(oAuth2UserDatabaseStrategy, endpointOptions, savedRequestRedisSuccessHandler, authenticationFailureHandler)
        );

        return httpSecurity
                .requestCache(httpSecurityRequestCacheConfigurer -> {
                    final var httpSessionRequestCache = new HttpSessionRequestCache();
                    httpSessionRequestCache.setRequestMatcher(new AntPathRequestMatcher("/oauth2/**"));

                    httpSecurityRequestCacheConfigurer.requestCache(httpSessionRequestCache);
                })
                .csrf(csrfConfigurerCustomizer(cookieCsrfTokenRepository, csrfTokenRequestHandler))
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

    /**
     * The oAuth2LoginConfigurerCustomizer function is a Spring Boot
     *
     * @param oAuth2UserDatabaseStrategy   Create a user database strategy
     * @param endpointOptions              Configure the login page
     * @param authenticationSuccessHandler Redirect the user to a specific page after successful login
     * @param authenticationFailureHandler Redirect the user to a page if they fail authentication
     * @return An instance of customizer&lt;oauth2loginconfigurer&lt;httpsecurity&gt;&gt;
     */
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
    private Customizer<CsrfConfigurer<HttpSecurity>> csrfConfigurerCustomizer(final CsrfTokenRepository csrfTokenRepository,
                                                                              final CsrfTokenRequestHandler csrfTokenRequestHandler) {
        return csrfConfigurer -> {
            csrfConfigurer.ignoringRequestMatchers(request -> HttpMethod.GET.name().equalsIgnoreCase(request.getMethod()));

            csrfConfigurer.csrfTokenRepository(csrfTokenRepository);
            csrfConfigurer.csrfTokenRequestHandler(csrfTokenRequestHandler);

            csrfConfigurer.sessionAuthenticationStrategy(new CsrfAuthenticationStrategy(csrfTokenRepository));
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
                .requestMatchers(HttpMethod.GET, UrlPatternEnum.toArray(UrlPatternEnum.CSRF)).permitAll()
                .requestMatchers(HttpMethod.GET, UrlPatternEnum.toArray(UrlPatternEnum.API_ACCOUNT)).permitAll()
                .requestMatchers(HttpMethod.POST, "/api/accounts/signin").permitAll()
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
