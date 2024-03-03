package by.sorface.sso.web.dao.repository;

import by.sorface.sso.web.dao.models.RegistryTokenEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistryTokenRepository extends BaseRepository<RegistryTokenEntity> {

    RegistryTokenEntity findByHashIgnoreCase(final String hash);

    RegistryTokenEntity findByUser_Email(final String email);

}
