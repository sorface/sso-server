package by.sorface.sso.web.services.tokens;

import by.sorface.sso.web.dao.models.TokenEntity;
import by.sorface.sso.web.dao.models.enums.TokenOperationType;

public interface TokenValidator {

    void validateOperation(final TokenEntity token, final TokenOperationType operationType);

    void validateExpiredDate(final TokenEntity token);

}
