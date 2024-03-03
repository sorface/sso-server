package by.sorface.sso.web.services.users;

import by.sorface.sso.web.dao.models.UserEntity;

public interface UserService {

    /**
     * Получение пользователя по логину
     *
     * @param username логин пользователя
     * @return пользователь
     */
    UserEntity findByUsername(final String username);

    /**
     * Получение пользователя по электронной почте
     *
     * @param email электронная почта пользователя
     * @return пользователь
     */
    UserEntity findByEmail(final String email);

    /**
     * Получение пользователя по логину или электронной почте
     *
     * @param username логин пользователя
     * @param email    электронная почта пользователя
     * @return пользователь
     */
    UserEntity findByUsernameOrEmail(final String username, final String email);

    /**
     * Создание нового пользователя
     *
     * @param user данные пользователя
     * @return пользователь
     */
    UserEntity save(final UserEntity user);
}
