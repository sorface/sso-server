package by.sorface.ssoserver.facade;

import by.sorface.ssoserver.UserRecord;
import by.sorface.ssoserver.records.responses.UserConfirm;
import by.sorface.ssoserver.records.responses.UserRegistered;

public interface UserFacadeService {

    UserRegistered registry(final UserRecord user);

    UserConfirm confirmEmail(final String token);

    UserRegistered resendConfirmEmail(final String email);

}
