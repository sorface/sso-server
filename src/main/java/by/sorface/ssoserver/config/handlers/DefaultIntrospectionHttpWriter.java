package by.sorface.ssoserver.config.handlers;

import by.sorface.ssoserver.records.IntrospectionPrincipal;
import by.sorface.ssoserver.records.SorfacePrincipal;
import by.sorface.ssoserver.records.TokenRecord;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenIntrospection;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenIntrospectionAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DefaultIntrospectionHttpWriter implements IntrospectionHttpWriter {

    private final static String principalAttributeKey = "java.security.Principal";

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Override
    public void write(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        final TokenRecord token = this.getToken((OAuth2TokenIntrospectionAuthenticationToken) authentication);

        final var outputMessage = new ServletServerHttpResponse(response);

        mappingJackson2HttpMessageConverter.write(token, null, outputMessage);
    }

    private TokenRecord getToken(final OAuth2TokenIntrospectionAuthenticationToken authenticationToken) {
        final var tokenRecordBuilder = TokenRecord.builder().active(false);

        if (!authenticationToken.getTokenClaims().isActive()) {
            return tokenRecordBuilder.build();
        }

        final OAuth2TokenIntrospection claims = authenticationToken.getTokenClaims();

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

        final String token = authenticationToken.getToken();

        final OAuth2Authorization tokenAuth = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

        if (Objects.isNull(tokenAuth)) {
            return tokenRecordBuilder.build();
        }

        final Authentication attributeAuth = tokenAuth.getAttribute(principalAttributeKey);

        if (Objects.isNull(attributeAuth)) {
            return tokenRecordBuilder.build();
        }

        if (attributeAuth.getPrincipal() instanceof SorfacePrincipal authorizedUser) {
            tokenRecordBuilder.principal(IntrospectionPrincipal.build(authorizedUser));
        } else{
            throw new RuntimeException("Principal class = " + attributeAuth.getPrincipal().getClass().getSimpleName() + " is not supported");
        }
        return tokenRecordBuilder.build();
    }

}
