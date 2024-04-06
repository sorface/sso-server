package by.sorface.sso.web.converters;

import by.sorface.sso.web.dao.models.OAuth2Client;
import by.sorface.sso.web.records.responses.ApplicationClient;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class OAuth2ClientConverter {

    public ApplicationClient convert(final OAuth2Client source) {
        return convert(source, source.getClientSecret());
    }

    public ApplicationClient convertWithoutSecret(final OAuth2Client source) {
        return convert(source, null);
    }

    public ApplicationClient convert(final OAuth2Client source, final String clientSecret) {
        return ApplicationClient.builder()
                .id(source.getId().toString())
                .clientId(source.getClientId())
                .clientName(source.getClientName())
                .clientSecret(clientSecret)
                .redirectUrls(Arrays.stream(source.getRedirectUris().split(";")).collect(Collectors.toSet()))
                .expiresAt(source.getClientSecretExpiresAt())
                .issueTime(source.getClientIdIssueAt())
                .build();
    }

}
