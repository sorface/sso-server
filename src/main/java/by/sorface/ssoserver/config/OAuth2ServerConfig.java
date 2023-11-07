package by.sorface.ssoserver.config;

import by.sorface.ssoserver.config.properties.AuthorizationServerProperties;
import by.sorface.ssoserver.constants.enums.UrlPatterns;
import by.sorface.ssoserver.records.IntrospectionPrincipal;
import by.sorface.ssoserver.records.SorfaceUser;
import by.sorface.ssoserver.records.TokenRecord;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenIntrospection;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenIntrospectionAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class OAuth2ServerConfig {

    private final static String principalAttributeKey = "java.security.Principal";

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    /**
     * Конфигурация OAuth2 Spring Security
     *
     * @param http configuring web based security
     * @return security chain
     * @throws Exception исключение ошибки настройки
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(final HttpSecurity http) throws Exception {
        final var authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        authorizationServerConfigurer.tokenIntrospectionEndpoint(oAuth2TokenIntrospectionEndpointConfigurer ->
                oAuth2TokenIntrospectionEndpointConfigurer.introspectionResponseHandler(this::introspectionResponse));

        final RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        http.securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(UrlPatterns.toArray()).permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(
                        new LoginUrlAuthenticationEntryPoint("/login"))
                )
                .apply(authorizationServerConfigurer);

        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(final AuthorizationServerProperties authorizationServerProperties) {
        return AuthorizationServerSettings.builder()
                .issuer(authorizationServerProperties.getIssuerUrl())
                .tokenIntrospectionEndpoint(authorizationServerProperties.getIntrospectionEndpoint())
                .build();
    }

    private void introspectionResponse(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        final var introspectionAuthenticationToken = (OAuth2TokenIntrospectionAuthenticationToken) authentication;

        final var tokenRecordBuilder = TokenRecord.builder().active(false);                        // создаём билдер объекта ответа

        final var outputMessage = new ServletServerHttpResponse(response);

        if (!introspectionAuthenticationToken.getTokenClaims().isActive()) {
            mappingJackson2HttpMessageConverter.write(tokenRecordBuilder.build(), null, outputMessage);
        }

        OAuth2TokenIntrospection claims = introspectionAuthenticationToken.getTokenClaims();

        tokenRecordBuilder
                .active(true)
                .sub(claims.getSubject())
                .aud(claims.getAudience())
                .nbf(claims.getNotBefore())
                .scopes(claims.getScopes())
                .iss(claims.getIssuer())
                .exp(claims.getExpiresAt())
                .iat(claims.getIssuedAt())
                .jti(claims.getId())
                .clientId(claims.getClientId())
                .tokenType(claims.getTokenType());


        String token = introspectionAuthenticationToken.getToken();

        // получаем значение токена, который проверяется
        final OAuth2Authorization tokenAuth = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);     // предполагая что это ACCESS TOKEN, пытаемся получить объект OAuth2Authorization из OAuth2AuthorizationService

        if (Objects.nonNull(tokenAuth)) {
            Authentication attributeAuth = tokenAuth.getAttribute(principalAttributeKey);                                // Если найден этот объект OAuth2Authorization, то получаем из него объект Authentication следующим образом
            if (attributeAuth != null) {
                if (attributeAuth.getPrincipal() instanceof SorfaceUser authorizedUser) {                             // Если полученный объект Authentication не пуст, то проверяем является ли его principal экземпляром класса AuthorizedUser
                    tokenRecordBuilder.principal(IntrospectionPrincipal.build(authorizedUser));                         // Создаём IntrospectionPrincipal на его основе
                } else {                                                                                                 // Иначе выбрасываем исключение, что другие типы principal мы не поддерживаем
                    throw new RuntimeException("Principal class = " + attributeAuth.getPrincipal().getClass().getSimpleName() + " is not supported");
                }
            }
        }
    }


}
