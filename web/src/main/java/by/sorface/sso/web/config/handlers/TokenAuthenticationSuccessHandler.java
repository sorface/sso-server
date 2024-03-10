package by.sorface.sso.web.config.handlers;

import by.sorface.sso.web.exceptions.ObjectInvalidException;
import by.sorface.sso.web.records.principals.SfPrincipal;
import by.sorface.sso.web.records.tokens.IntrospectionPrincipal;
import by.sorface.sso.web.records.tokens.TokenRecord;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenIntrospection;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenIntrospectionAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final static String principalAttributeKey = "java.security.Principal";

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        final var token = getToken((OAuth2TokenIntrospectionAuthenticationToken) authentication);
        final var message = new ServletServerHttpResponse(response);

        mappingJackson2HttpMessageConverter.write(token, MediaType.APPLICATION_JSON, message);
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

        final var oAuth2Authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

        if (Objects.isNull(oAuth2Authorization)) {
            return tokenRecordBuilder.build();
        }

        final Authentication attributeAuth = oAuth2Authorization.getAttribute(principalAttributeKey);

        if (Objects.isNull(attributeAuth)) {
            return tokenRecordBuilder.build();
        }

        if (attributeAuth.getPrincipal() instanceof SfPrincipal authorizedUser) {
            final var introspectionPrincipal = IntrospectionPrincipal.from(authorizedUser);

            tokenRecordBuilder.principal(introspectionPrincipal);
        } else {
            throw new ObjectInvalidException("Principal class = " + attributeAuth.getPrincipal().getClass().getSimpleName() + " is not supported");
        }

        return tokenRecordBuilder.build();
    }

}
