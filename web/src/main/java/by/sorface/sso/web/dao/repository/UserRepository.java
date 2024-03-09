package by.sorface.sso.web.dao.repository;

import by.sorface.sso.web.dao.models.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<UserEntity> {

    /**
     * Поиск пользователя по логину
     *
     * @param username логин пользователя
     * @return пользователь
     */
    UserEntity findFirstByUsernameIgnoreCase(final String username);

    /**
     * Поиск пользователя по элетронной почте
     *
     * @param email элетронная почта
     * @return пользователь
     */
    UserEntity findFirstByEmailIgnoreCase(final String email);

    /**
     * Поиск пользователя по логину или электронной почте
     *
     * @param username логин пользователя
     * @param email    email пользователя
     * @return пользователь
     */
    UserEntity findFirstByUsernameIgnoreCaseOrEmailIgnoreCase(final String username, final String email);

}
