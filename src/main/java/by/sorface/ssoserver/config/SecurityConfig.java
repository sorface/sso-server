package by.sorface.ssoserver.config;

import by.sorface.ssoserver.services.providers.OAuth2UserDatabaseProvider;
import by.sorface.ssoserver.services.providers.SorfaceUserDatabaseProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity(debug = true)
@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;

    private final OAuth2UserDatabaseProvider OAuth2UserDatabaseProvider;

    private final SorfaceUserDatabaseProvider sorfaceUserDatabaseProvider;

    private final AuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler();

    private final AuthenticationSuccessHandler authenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();

    public static final String[] PERMIT_ALL_PATTERNS = {
            "/static/**",
            "/login"
    };


    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(sorfaceUserDatabaseProvider)
                .passwordEncoder(passwordEncoder);

        return http
                .csrf()
                .disable()
                .oauth2Login(configurer -> {
                    final var userInfoEndpointConfig = configurer.userInfoEndpoint();

                    userInfoEndpointConfig.userService(OAuth2UserDatabaseProvider);

                    configurer.failureHandler(authenticationFailureHandler);
                    configurer.successHandler(authenticationSuccessHandler);
                })
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers("/api/**").permitAll()
                            .requestMatchers(PERMIT_ALL_PATTERNS).permitAll()
                            .anyRequest().authenticated();
                })
                .formLogin(withDefaults())
                .build();
    }

}
