package by.sorface.sso.web.services.tokens;

import by.sorface.sso.web.dao.models.TokenEntity;
import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.dao.models.enums.TokenOperationType;

public interface TokenService {

    TokenEntity findByHash(final String hash);

    TokenEntity saveForUser(final UserEntity user, final TokenOperationType operationType);

    TokenEntity findByEmail(final String email);

    void deleteByHash(final String hash);

}
