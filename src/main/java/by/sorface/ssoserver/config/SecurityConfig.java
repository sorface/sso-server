package by.sorface.ssoserver.config;

import by.sorface.ssoserver.constants.enums.UrlPatterns;
import by.sorface.ssoserver.services.providers.OAuth2UserDatabaseProvider;
import by.sorface.ssoserver.services.providers.SorfaceUserDatabaseProvider;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String LOGIN_PAGE = "/api/login";

    private final OAuth2UserDatabaseProvider oAuth2UserDatabaseProvider;

    private AuthenticationSuccessHandler oAuth2successHandler;
    private AuthenticationSuccessHandler loginRequestSuccessHandler;

    private AuthenticationFailureHandler failureHandler;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        final var socialConfigurer = new SocialConfigurer()
                .oAuth2UserService(oAuth2UserDatabaseProvider)
                .successHandler(oAuth2successHandler)
                .failureHandler(failureHandler)
                .formLogin(LOGIN_PAGE);

        http.apply(socialConfigurer);
        http.csrf(AbstractHttpConfigurer::disable);

        http.getSharedObject(AuthenticationManagerBuilder.class);

        http.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers(UrlPatterns.toArray()).permitAll()
                        .anyRequest().authenticated()
        );

        return http
                .formLogin(configurer ->
                        configurer.loginPage(LOGIN_PAGE)
                                .loginProcessingUrl(LOGIN_PAGE)
                                .successHandler(loginRequestSuccessHandler)
                                .failureHandler(failureHandler))
                .build();
    }

    @PostConstruct
    private void initializeHandlers() {
        this.loginRequestSuccessHandler = new SorfaceAuthenticationSuccessHandler(
                "http://localhost:8080/", "J-Sso-Next-Location"
        );

        this.oAuth2successHandler = new SimpleUrlAuthenticationSuccessHandler("http://localhost:8080/");

        this.failureHandler = (request, response, exception) -> response.setStatus(HttpStatus.BAD_REQUEST.value());
    }

}
