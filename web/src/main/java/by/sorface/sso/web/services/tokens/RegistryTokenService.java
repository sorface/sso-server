package by.sorface.sso.web.services.tokens;

import by.sorface.sso.web.dao.models.RegistryTokenEntity;
import by.sorface.sso.web.dao.models.UserEntity;

public interface RegistryTokenService {

    RegistryTokenEntity findByHash(final String hash);

    RegistryTokenEntity saveRandomForUser(final UserEntity user);

    RegistryTokenEntity findRegistryTokenByUserEmail(final String email);

}
