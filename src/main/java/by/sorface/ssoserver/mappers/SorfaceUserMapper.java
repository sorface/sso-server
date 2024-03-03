package by.sorface.ssoserver.mappers;

import by.sorface.ssoserver.dao.models.UserEntity;
import by.sorface.ssoserver.records.SfPrincipal;

public interface SorfaceUserMapper {

    /**
     * Преобразование доменного пользователя в principle
     *
     * @param user доменный пользователь
     * @return principle
     */
    SfPrincipal to(final UserEntity user);

}
