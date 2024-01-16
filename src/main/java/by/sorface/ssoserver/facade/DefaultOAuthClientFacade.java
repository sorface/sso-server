package by.sorface.ssoserver.facade;

import by.sorface.ssoserver.exceptions.ObjectInvalidException;
import by.sorface.ssoserver.records.OAuthClient;
import by.sorface.ssoserver.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultOAuthClientFacade implements OAuthClientFacade {

    public final ClientService clientService;

    @Override
    public void create(final OAuthClient oAuthClient) {
        if (Objects.isNull(oAuthClient.getRedirectUrls()) || oAuthClient.getRedirectUrls().isEmpty()) {
            throw new ObjectInvalidException("Redirect URLs not found");
        }

        if (Objects.isNull(oAuthClient.getClientName())) {
            throw new ObjectInvalidException("client id not found");
        }

        final var registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientName(oAuthClient.getClientName())
                .redirectUris(urls -> urls.addAll(oAuthClient.getRedirectUrls()))
                .clientIdIssuedAt(Instant.now().plus(1, ChronoUnit.YEARS))
                .clientSecretExpiresAt(Instant.now().plus(3, ChronoUnit.YEARS))
                .build();

        clientService.save(registeredClient);
    }

}
