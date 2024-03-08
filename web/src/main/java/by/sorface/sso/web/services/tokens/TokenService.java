package by.sorface.sso.web.services.tokens;

import by.sorface.sso.web.dao.models.TokenEntity;
import by.sorface.sso.web.dao.models.UserEntity;

public interface TokenService {

    TokenEntity findByHash(final String hash);

    TokenEntity saveForUser(final UserEntity user);

    TokenEntity findTokenByEmail(final String email);

}
