package by.sorface.sso.web.services.users;

import by.sorface.sso.web.dao.models.RoleEntity;

public interface RoleService {

    /**
     * Find role by value
     *
     * @param value role value
     * @return role
     */
    RoleEntity findByValue(final String value);

}
