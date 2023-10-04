package by.sorface.ssoserver.services;

import by.sorface.ssoserver.dao.models.UserEntity;

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
     * @param email логин пользователя
     * @return пользователь
     */
    UserEntity findByEmail(final String email);

    /**
     * Создание нового пользователя
     *
     * @param user данные пользователя
     * @return пользователь
     */
    UserEntity save(final UserEntity user);
}
