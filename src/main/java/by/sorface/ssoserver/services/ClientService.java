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

    private final OAuth2ClientService oAuth2ClientService;

    @Override
    public void save(final RegisteredClient registeredClient) {
        final var oAuth2Client = new OAuth2Client();
        {
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
        return RegisteredClient.withId(oAuth2Client.getId().toString())
                .clientId(oAuth2Client.getClientId())
                .clientSecret(oAuth2Client.getClientSecret())
                .clientIdIssuedAt(oAuth2Client.getClientIdIssueAt().toInstant(ZoneOffset.UTC))
                .clientSecretExpiresAt(oAuth2Client.getClientSecretExpiresAt().toInstant(ZoneOffset.UTC))
                .clientName(oAuth2Client.getClientName())
                .clientAuthenticationMethods(clientAuthenticationMethods -> clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC))
                .authorizationGrantTypes(authorizationGrantTypes -> authorizationGrantTypes.addAll(Set.of(AuthorizationGrantType.AUTHORIZATION_CODE, AuthorizationGrantType.REFRESH_TOKEN)))
                .redirectUris(redirectUris -> redirectUris.addAll(getRedirectUrls(oAuth2Client.getRedirectUris(), ";")))
                .scopes(scopes -> scopes.addAll(Set.of("scope.read", "scope.write")))
                .tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE).build())
                .build();
    }

    private List<String> getRedirectUrls(final String value, final String spliterator) {
        return Arrays.stream(value.split(spliterator)).toList();
    }

}
