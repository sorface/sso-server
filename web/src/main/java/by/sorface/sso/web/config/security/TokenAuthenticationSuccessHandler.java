package by.sorface.sso.web.config.security;

import by.sorface.sso.web.exceptions.ObjectInvalidException;
import by.sorface.sso.web.records.principals.DefaultPrincipal;
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

    /**
     * The onAuthenticationSuccess function is called when the authentication process succeeds.
     * It takes in a request, response, and authentication object as parameters.
     * The function then creates a token using the getToken function (which we will define later).
     * Then it creates an HttpMessageConverter that can convert Java objects to JSON strings.
     *
     * @param request        Get the httpservletrequest object
     * @param response       Write the token to the response body
     * @param authentication Get the token
     *                       private tokenresponse gettoken(final oauth2tokenintrospectionauthenticationtoken authentication) {
     *                       final var principal = (oauth2user) authentication
     */
    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
        final var token = getToken((OAuth2TokenIntrospectionAuthenticationToken) authentication);
        final var message = new ServletServerHttpResponse(response);

        mappingJackson2HttpMessageConverter.write(token, MediaType.APPLICATION_JSON, message);
    }

    /**
     * The getToken function is responsible for retrieving the token record from the database.
     * <p>
     * The function takes in an OAuth2TokenIntrospectionAuthenticationToken object and returns a TokenRecord object.
     * The function first creates a new TokenRecordBuilder object, which will be used to build our final TokenRecord object.
     * <p>
     * Next, we check if the token is active by checking if it's active property is true or false.
     * If it's not active, then we return an empty TokenRecordBuilder with its &quot;active&quot; property set to false (line 12).
     * Otherwise, we continue on with building our final token record (lines 14-15)
     *
     * @param authenticationToken Get the token claims
     * @return An object of type tokenrecord
     */
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

        if (attributeAuth.getPrincipal() instanceof DefaultPrincipal principal) {
            final var introspectionPrincipal = IntrospectionPrincipal.from(principal);

            tokenRecordBuilder.principal(introspectionPrincipal);
        } else {
            throw new ObjectInvalidException("Principal class = " + attributeAuth.getPrincipal().getClass().getSimpleName() + " is not supported");
        }

        return tokenRecordBuilder.build();
    }

}
