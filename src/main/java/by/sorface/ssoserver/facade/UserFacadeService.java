package by.sorface.ssoserver.facade;

import by.sorface.ssoserver.UserRecord;

public interface UserFacadeService {

    UserRegistryFacade.UserRegistered executeProcessRegistry(final UserRecord user);

    UserRegistryFacade.UserConfirm executeProcessConfirmation(final String token);

}
