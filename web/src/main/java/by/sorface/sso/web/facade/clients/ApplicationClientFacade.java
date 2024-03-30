package by.sorface.sso.web.facade.clients;

import by.sorface.sso.web.records.requests.ApplicationRegistry;
import by.sorface.sso.web.records.responses.ApplicationClient;
import by.sorface.sso.web.records.responses.ApplicationClientRefreshSecret;

import java.util.List;
import java.util.UUID;

public interface ApplicationClientFacade {

    ApplicationClient registry(final ApplicationRegistry registryClient);

    List<ApplicationClient> findByCurrentUser();

    ApplicationClient getByIdAndCurrentUser(final UUID clientId);

    ApplicationClientRefreshSecret refreshSecret(final UUID clientId);

}
