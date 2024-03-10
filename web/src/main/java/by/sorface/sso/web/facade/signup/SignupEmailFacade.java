package by.sorface.sso.web.facade.signup;

import by.sorface.sso.web.records.requests.AccountSignup;
import by.sorface.sso.web.records.requests.ResendConfirmEmail;
import by.sorface.sso.web.records.responses.UserRegistered;

/**
 *
 */
public interface SignupEmailFacade {

    /**
     * The process user registration in the system
     *
     * @param user данные о пользователе
     * @return data of the registered user
     */
    UserRegistered signup(final AccountSignup user);

    /**
     * Resend confirm mail to user's email
     *
     * @param email user's email
     * @return info by confirmation email
     */
    UserRegistered resendConfirmEmail(final ResendConfirmEmail email);

}
