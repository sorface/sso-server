package by.sorface.sso.web.facade.signup;

import by.sorface.sso.web.exceptions.UserRequestException;
import by.sorface.sso.web.records.requests.AccountSignup;
import by.sorface.sso.web.records.requests.ConfirmEmail;
import by.sorface.sso.web.records.responses.UserConfirm;
import by.sorface.sso.web.records.responses.UserRegisteredHash;

/**
 *
 */
public interface SignupFacade {

    UserRegisteredHash signup(final AccountSignup user) throws UserRequestException;

    UserConfirm confirm(final ConfirmEmail token);

    UserRegisteredHash findTokenByEmail(final String email);

}
