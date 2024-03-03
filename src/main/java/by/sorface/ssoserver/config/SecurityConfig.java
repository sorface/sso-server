package by.sorface.ssoserver.config;

import by.sorface.ssoserver.config.handlers.AuthLoginFailureHandler;
import by.sorface.ssoserver.config.handlers.AuthLoginSuccessHandler;
import by.sorface.ssoserver.config.properties.MvcLoginProperties;
import by.sorface.ssoserver.constants.enums.UrlPatternEnum;
import by.sorface.ssoserver.services.providers.OAuth2UserDatabaseProvider;
import by.sorface.ssoserver.services.providers.OidcUserDatabaseProvider;
import by.sorface.ssoserver.services.providers.SorfaceUserDatabaseProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SecurityConfig extends SecurityConfigurerAdapter {

    private final OAuth2UserDatabaseProvider oAuth2UserDatabaseProvider;

    private final OidcUserDatabaseProvider oidcUserDatabaseProvider;

    private final MvcLoginProperties mvcLoginProperties;

    @Bean
    public AuthenticationFailureHandler authLoginFailureHandler() {
        return new AuthLoginFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthLoginSuccessHandler();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(final HttpSecurity http,
                                                          final AuthenticationFailureHandler failureHandler,
                                                          final AuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {
        http.getSharedObject(AuthenticationManagerBuilder.class);

        http.oauth2Login(configurer -> {
            configurer.userInfoEndpoint(userInfoEndpointConfig ->
                    userInfoEndpointConfig
                            .userService(oAuth2UserDatabaseProvider)
                            .oidcUserService(oidcUserDatabaseProvider));
        });

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(UrlPatternEnum.toArray()).permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(configurer -> {
                    configurer.loginPage(mvcLoginProperties.getPageEndpoint());
                    configurer.loginProcessingUrl(mvcLoginProperties.getApiEndpoint());

                    configurer.successHandler(authenticationSuccessHandler);
                    configurer.failureHandler(failureHandler);
                })
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(SorfaceUserDatabaseProvider userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        final var authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

}
