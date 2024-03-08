package by.sorface.sso.web.dao.repository;

import by.sorface.sso.web.dao.models.TokenEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistryTokenRepository extends BaseRepository<TokenEntity> {

    TokenEntity findByHashIgnoreCase(final String hash);

    TokenEntity findByUser_Email(final String email);

}
