package by.sorface.ssoserver.config;

import by.sorface.ssoserver.constants.enums.UrlPatterns;
import by.sorface.ssoserver.services.providers.OAuth2UserDatabaseProvider;
import by.sorface.ssoserver.services.providers.OidcUserDatabaseProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.util.Arrays;

@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SecurityConfig extends SecurityConfigurerAdapter {

    private final OAuth2UserDatabaseProvider oAuth2UserDatabaseProvider;

    private final OidcUserDatabaseProvider oidcUserDatabaseProvider;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.getSharedObject(AuthenticationManagerBuilder.class);

        http.oauth2Login(configurer -> {
            configurer.userInfoEndpoint(userInfoEndpointConfig -> {
                userInfoEndpointConfig
                        .userService(oAuth2UserDatabaseProvider)
                        .oidcUserService(oidcUserDatabaseProvider);
            });
            configurer.loginPage("/login");
        });

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(UrlPatterns.toArray()).permitAll()
                                .requestMatchers("/css/**, /js/**, /images/**, /webjars/**, **/favicon.ico").permitAll()
                                .anyRequest().authenticated())
                .formLogin(httpSecurityFormLoginConfigurer -> {
                    httpSecurityFormLoginConfigurer.loginPage("/login");
                    httpSecurityFormLoginConfigurer.loginProcessingUrl("/login");
                })
                .build();
    }

}
