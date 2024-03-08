package by.sorface.sso.web.mappers;

import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.records.requests.AccountSignup;

public interface UserMapper {

    UserEntity map(final AccountSignup accountSignup);

}
