package by.sorface.ssoserver.mappers;

import by.sorface.ssoserver.UserRecord;
import by.sorface.ssoserver.dao.models.UserEntity;

public interface UserToUserEntityMapper {

    UserEntity map(final UserRecord userRecord);

}
