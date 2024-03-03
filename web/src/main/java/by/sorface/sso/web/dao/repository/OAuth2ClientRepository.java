package by.sorface.sso.web.dao.repository;

import by.sorface.sso.web.dao.models.OAuth2Client;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2ClientRepository extends BaseRepository<OAuth2Client> {

    OAuth2Client findByClientId(final String id);

}
