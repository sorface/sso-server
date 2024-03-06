package by.sorface.sso.web.config;

import by.sorface.sso.web.config.properties.MvcLoginProperties;
import by.sorface.sso.web.constants.enums.UrlPatternEnum;
import by.sorface.sso.web.services.providers.OAuth2UserDatabaseProvider;
import by.sorface.sso.web.services.providers.OidcUserDatabaseProvider;
import by.sorface.sso.web.services.providers.SorfaceUserDatabaseProvider;
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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SfSecurityConfig extends SecurityConfigurerAdapter {

    private final OAuth2UserDatabaseProvider oAuth2UserDatabaseProvider;

    private final OidcUserDatabaseProvider oidcUserDatabaseProvider;

    private final MvcLoginProperties mvcLoginProperties;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(final HttpSecurity http) throws Exception {
        http.getSharedObject(AuthenticationManagerBuilder.class);

        http.oauth2Login(configurer -> {
            configurer.userInfoEndpoint(configure -> {
                configure.userService(oAuth2UserDatabaseProvider);
                configure.oidcUserService(oidcUserDatabaseProvider);
            });

            configurer.loginPage(mvcLoginProperties.getPageEndpoint());
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

                    configurer.successHandler(new SimpleUrlAuthenticationSuccessHandler("/home"));
                    configurer.failureHandler(new SimpleUrlAuthenticationFailureHandler("/error"));
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
