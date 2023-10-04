package by.sorface.ssoserver.dao.repository;

import by.sorface.ssoserver.dao.models.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<UserEntity> {

    /**
     * Поиск пользователя по логину
     *
     * @param username логин пользователя
     * @return пользователь
     */
    UserEntity findFirstByUsername(final String username);

    /**
     * Поиск пользователя по элетронной почте
     *
     * @param email элетронная почта
     * @return пользователь
     */
    UserEntity findFirstByEmail(final String email);

}
