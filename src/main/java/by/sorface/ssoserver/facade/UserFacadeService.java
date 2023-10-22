package by.sorface.ssoserver.facade;

import by.sorface.ssoserver.UserRecord;
import by.sorface.ssoserver.records.responses.UserConfirm;
import by.sorface.ssoserver.records.responses.UserRegistered;

public interface UserFacadeService {

    UserRegistered executeProcessRegistry(final UserRecord user);

    UserConfirm executeProcessConfirmation(final String token);

}
