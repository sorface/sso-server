package by.sorface.sso.web.mappers;

import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.records.SfPrincipal;

public interface SfUserMapper {

    /**
     * Преобразование доменного пользователя в principle
     *
     * @param user доменный пользователь
     * @return principle
     */
    SfPrincipal to(final UserEntity user);

}
