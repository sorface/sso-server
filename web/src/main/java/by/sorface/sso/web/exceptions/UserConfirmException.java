package by.sorface.sso.web.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserConfirmException extends AuthenticationException {

    public UserConfirmException(final String message) {
        super(message);
    }

}
