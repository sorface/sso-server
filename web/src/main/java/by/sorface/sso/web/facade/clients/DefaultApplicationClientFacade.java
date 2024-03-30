package by.sorface.sso.web.facade.clients;

import by.sorface.sso.web.converters.OAuth2ClientConverter;
import by.sorface.sso.web.dao.models.OAuth2Client;
import by.sorface.sso.web.exceptions.NotFoundException;
import by.sorface.sso.web.records.principals.DefaultPrincipal;
import by.sorface.sso.web.records.requests.ApplicationRegistry;
import by.sorface.sso.web.records.responses.ApplicationClient;
import by.sorface.sso.web.records.responses.ApplicationClientRefreshSecret;
import by.sorface.sso.web.services.clients.OAuth2ClientService;
import by.sorface.sso.web.utils.HashUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultApplicationClientFacade implements ApplicationClientFacade {

    private final OAuth2ClientService oAuth2ClientService;

    private final OAuth2ClientConverter oAuth2ClientConverter;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<ApplicationClient> findByCurrentUser() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        final var principal = (DefaultPrincipal) authentication.getPrincipal();

        return oAuth2ClientService.findAllByUserId(principal.getId()).stream()
                .map(oAuth2ClientConverter::convertWithoutSecret)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationClient getByIdAndCurrentUser(UUID clientId) {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        final var principal = (DefaultPrincipal) authentication.getPrincipal();

        OAuth2Client oAuth2Client = oAuth2ClientService.findByIdAndUserId(clientId, principal.getId());

        if (Objects.isNull(oAuth2Client)) {
            throw new NotFoundException("application.client.not-found-by-id");
        }

        return oAuth2ClientConverter.convertWithoutSecret(oAuth2Client);
    }

    @Override
    public ApplicationClientRefreshSecret refreshSecret(final UUID clientId) {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        final var principal = (DefaultPrincipal) authentication.getPrincipal();

        final var oAuth2Client = oAuth2ClientService.findByIdAndUserId(clientId, principal.getId());

        if (Objects.isNull(oAuth2Client)) {
            throw new NotFoundException("application.client.not-found-by-id");
        }

        final var clientSecret = generateClientSecret();

        oAuth2Client.setClientSecret(passwordEncoder.encode(clientSecret));
        oAuth2Client.setClientSecretExpiresAt(buildDefaultClientSecretExpire());

        oAuth2ClientService.save(oAuth2Client);

        return ApplicationClientRefreshSecret.builder()
                .clientSecret(clientSecret)
                .expiresAt(buildDefaultClientSecretExpire())
                .build();
    }

    @Override
    public ApplicationClient registry(final ApplicationRegistry registryClient) {
        final var clientSecret = generateClientSecret();

        final var oAuth2Client = new OAuth2Client();
        {
            oAuth2Client.setClientId(HashUtils.generateRegistryHash(55) + "@sorface.oauth.client");
            oAuth2Client.setClientSecret(passwordEncoder.encode(clientSecret));
            oAuth2Client.setClientName(registryClient.name());
            oAuth2Client.setClientIdIssueAt(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
            oAuth2Client.setClientSecretExpiresAt(buildDefaultClientSecretExpire());
            oAuth2Client.setRedirectUris(String.join(";", registryClient.redirectionUrls()));
        }

        final var oAuth2ClientSaved = oAuth2ClientService.save(oAuth2Client);

        return oAuth2ClientConverter.convert(oAuth2ClientSaved, clientSecret);
    }

    private LocalDateTime buildDefaultClientSecretExpire() {
        return LocalDateTime.ofInstant(Instant.now().plus(300, ChronoUnit.DAYS), ZoneOffset.UTC);
    }

    private String generateClientSecret() {
        return HashUtils.generateRegistryHash(55);
    }

}
