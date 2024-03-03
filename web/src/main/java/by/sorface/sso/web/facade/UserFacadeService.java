package by.sorface.sso.web.facade;

import by.sorface.sso.web.records.UserRegistryRecord;
import by.sorface.sso.web.records.responses.UserConfirm;
import by.sorface.sso.web.records.responses.UserRegistered;

/**
 * User Account Management Service
 */
public interface UserFacadeService {

    /**
     * The process user registration in the system
     *
     * @param user данные о пользователе
     * @return data of the registered user
     */
    UserRegistered registry(final UserRegistryRecord user);

    /**
     * Account confirmation by token
     *
     * @param token secret user's token
     * @return info by confirmation email
     */
    UserConfirm confirmEmail(final String token);

    /**
     * Resend confirm mail to user's email
     *
     * @param email user's email
     * @return info by confirmation email
     */
    UserRegistered resendConfirmationEmail(final String email);

}
