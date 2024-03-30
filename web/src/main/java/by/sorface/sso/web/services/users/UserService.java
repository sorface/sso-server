package by.sorface.sso.web.services.users;

import by.sorface.sso.web.dao.models.UserEntity;

import java.util.UUID;

public interface UserService {

    UserEntity findById(final UUID id);

    /**
     * Getting a user by login
     *
     * @param username user's login
     * @return user
     */
    UserEntity findByUsername(final String username);

    /**
     * Receiving a user by email
     *
     * @param email user's email address
     * @return user
     */
    UserEntity findByEmail(final String email);

    /**
     * Receiving a user by login or email
     *
     * @param username user's login
     * @param email    user's email address
     * @return user
     */
    UserEntity findByUsernameOrEmail(final String username, final String email);

    /**
     * Creating a new user
     *
     * @param user user data
     * @return user
     */
    UserEntity save(final UserEntity user);
}
