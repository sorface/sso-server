package by.sorface.sso.web.services.clients;

import by.sorface.sso.web.dao.models.OAuth2Client;

import java.util.UUID;

public interface OAuth2ClientService {

    OAuth2Client save(final OAuth2Client oAuth2Client);

    OAuth2Client findByClientId(final String clientId);

    OAuth2Client findById(final UUID id);

}
