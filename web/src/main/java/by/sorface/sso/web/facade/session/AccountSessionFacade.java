package by.sorface.sso.web.facade.session;

import by.sorface.sso.web.records.sessions.CleanupSession;
import by.sorface.sso.web.records.sessions.UserContextSession;

public interface AccountSessionFacade {

    UserContextSession findByUsername(final String username);

    UserContextSession getCurrentActiveSessions();

    UserContextSession deleteSessions(final CleanupSession cleanupSession);

    void batchDelete(final CleanupSession cleanupSession);

}
