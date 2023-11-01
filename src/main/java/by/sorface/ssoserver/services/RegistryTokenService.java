package by.sorface.ssoserver.services;

import by.sorface.ssoserver.dao.models.RegistryTokenEntity;
import by.sorface.ssoserver.dao.models.UserEntity;

public interface RegistryTokenService {

    RegistryTokenEntity findByHash(final String hash);

    RegistryTokenEntity saveRandomForUser(final UserEntity user);

    RegistryTokenEntity findRegistryTokenByUserEmail(final String email);

}
