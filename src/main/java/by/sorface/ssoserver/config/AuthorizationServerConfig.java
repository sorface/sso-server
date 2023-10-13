package by.sorface.ssoserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

    private final AuthorizationServerProperties authorizationServerProperties;

    private final SorfaceTokenProperties sorfaceTokenProperties;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        return http
                .exceptionHandling(exceptions -> {
                    final var authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint("/login");
                    exceptions.authenticationEntryPoint(authenticationEntryPoint);
                })
                .build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        final var tokenSettings = TokenSettings.builder()
                .accessTokenFormat(new OAuth2TokenFormat(sorfaceTokenProperties.getFormat()))
                .accessTokenTimeToLive(Duration.of(sorfaceTokenProperties.getAccessTimeLive(), ChronoUnit.MINUTES))
                .refreshTokenTimeToLive(Duration.of(sorfaceTokenProperties.getRefreshTimeLive(), ChronoUnit.MINUTES))
                .reuseRefreshTokens(sorfaceTokenProperties.isReuseRefreshToken())
                .authorizationCodeTimeToLive(Duration.of(sorfaceTokenProperties.getAuthorizationCodeTimeLive(), ChronoUnit.SECONDS))
                .build();

        final var defaultClient = RegisteredClient.withId("test-client-id")
                .clientName("Test Client")
                .clientId("test-client")
                .clientSecret("{noop}test-client")
                .redirectUri("http://127.0.0.1:8080/code")
                .scope("read.scope")
                .scope("write.scope")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .tokenSettings(tokenSettings)
                .build();

        return new InMemoryRegisteredClientRepository(defaultClient);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(authorizationServerProperties.getIssuerUrl())
                .tokenIntrospectionEndpoint(authorizationServerProperties.getIntrospectionEndpoint())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
