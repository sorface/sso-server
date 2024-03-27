package by.sorface.sso.web.records.sessions;

import java.util.List;

public record UserContextSession(List<UserSession> sessions) {
}
