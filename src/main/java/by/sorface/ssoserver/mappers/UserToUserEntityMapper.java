package by.sorface.ssoserver.mappers;

import by.sorface.ssoserver.records.UserRegistryRecord;
import by.sorface.ssoserver.dao.models.UserEntity;

public interface UserToUserEntityMapper {

    UserEntity map(final UserRegistryRecord userRegistryRecord);

}
