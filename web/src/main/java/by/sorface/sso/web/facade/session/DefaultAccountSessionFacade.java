package by.sorface.sso.web.facade.session;

import by.sorface.sso.web.records.sessions.CleanupSession;
import by.sorface.sso.web.records.sessions.UserContextSession;
import by.sorface.sso.web.services.sessions.AccountSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultAccountSessionFacade implements AccountSessionFacade {

    private final AccountSessionService accountSessionService;

    @Override
    public UserContextSession findByUsername(final String username) {
        List<Session> sessions = accountSessionService.findByUsername(username);

        final List<String> sessionIds = sessions.stream().map(Session::getId).collect(Collectors.toList());

        return new UserContextSession(sessionIds);
    }

    @Override
    public void batchDelete(final CleanupSession cleanupSession) {
        accountSessionService.batchDelete(cleanupSession.sessions());
    }

}
