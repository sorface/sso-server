package by.sorface.ssoserver.services;

import by.sorface.ssoserver.dao.models.OAuth2Client;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService implements RegisteredClientRepository {

    public static final String REDIRECT_URL_SPLITERATOR = ";";

    private final OAuth2ClientService oAuth2ClientService;

    @Override
    public void save(final RegisteredClient registeredClient) {
        final var oAuth2Client = new OAuth2Client();
        {
            oAuth2Client.setId(UUID.fromString(registeredClient.getId()));
            oAuth2Client.setClientId(registeredClient.getClientId());
            oAuth2Client.setClientName(registeredClient.getClientName());
            oAuth2Client.setClientSecret(registeredClient.getClientSecret());

            final var clientIdIssueAt = registeredClient.getClientIdIssuedAt() != null
                    ? LocalDateTime.ofInstant(registeredClient.getClientIdIssuedAt(), ZoneOffset.UTC)
                    : null;

            oAuth2Client.setClientIdIssueAt(clientIdIssueAt);

            final LocalDateTime clientSecretExpiresAt = registeredClient.getClientSecretExpiresAt() != null
                    ? LocalDateTime.ofInstant(registeredClient.getClientSecretExpiresAt(), ZoneOffset.UTC)
                    : null;

            oAuth2Client.setClientSecretExpiresAt(clientSecretExpiresAt);
            oAuth2Client.setRedirectUris(String.join(REDIRECT_URL_SPLITERATOR, registeredClient.getRedirectUris()));
        }

        oAuth2ClientService.save(oAuth2Client);
    }

    @Override
    public RegisteredClient findById(final String id) {
        final OAuth2Client oAuth2Client = oAuth2ClientService.findById(UUID.fromString(id));

        return this.buildClient(oAuth2Client);
    }

    @Override
    public RegisteredClient findByClientId(final String clientId) {
        final OAuth2Client oAuth2Client = oAuth2ClientService.findByClientId(clientId);

        return buildClient(oAuth2Client);
    }

    private RegisteredClient buildClient(final OAuth2Client oAuth2Client) {
        final var redirectUrls = this.getRedirectUrls(oAuth2Client.getRedirectUris());
        final var scopes = Set.of("scope.read", "scope.write");

        final var tokenSettings = TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                .build();

        final var authorizationCodes = Set.of(
                AuthorizationGrantType.AUTHORIZATION_CODE,
                AuthorizationGrantType.REFRESH_TOKEN
        );

        final var clientSecretBasic = ClientAuthenticationMethod.CLIENT_SECRET_BASIC;

        return RegisteredClient
                .withId(oAuth2Client.getId().toString())
                .clientId(oAuth2Client.getClientId())
                .clientSecret(oAuth2Client.getClientSecret())
                .clientIdIssuedAt(oAuth2Client.getClientIdIssueAt().toInstant(ZoneOffset.UTC))
                .clientSecretExpiresAt(oAuth2Client.getClientSecretExpiresAt().toInstant(ZoneOffset.UTC))
                .clientName(oAuth2Client.getClientName())
                .clientAuthenticationMethods(clientAuthenticationMethods -> clientAuthenticationMethods.add(clientSecretBasic))
                .authorizationGrantTypes(authorizationGrantTypes -> authorizationGrantTypes.addAll(authorizationCodes))
                .redirectUris(redirectUris -> redirectUris.addAll(redirectUrls))
                .scopes(scopeFunc -> scopeFunc.addAll(scopes))
                .tokenSettings(tokenSettings)
                .build();
    }

    private List<String> getRedirectUrls(final String value) {
        return Arrays.stream(value.split(ClientService.REDIRECT_URL_SPLITERATOR)).toList();
    }

}
