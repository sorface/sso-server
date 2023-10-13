package by.sorface.ssoserver.dao.repository;

import by.sorface.ssoserver.dao.models.OAuth2Client;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2ClientRepository extends BaseRepository<OAuth2Client> {

    OAuth2Client findByClientId(final String id);

}
