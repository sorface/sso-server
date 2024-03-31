package by.sorface.sso.web.services.clients;

import by.sorface.sso.web.dao.models.OAuth2Client;

import java.util.List;
import java.util.UUID;

public interface OAuth2ClientService {

    OAuth2Client save(final OAuth2Client oAuth2Client);

    OAuth2Client findByClientId(final String clientId);

    OAuth2Client findById(final UUID id);

    List<OAuth2Client> findAllByUserId(final UUID id);

    OAuth2Client findByIdAndUserId(final UUID id, final UUID userId);

    boolean isExists(final UUID id);

    void delete(final UUID id);

}
