package by.sorface.sso.web.facade.accounts;

import by.sorface.sso.web.records.requests.UserPatchUpdate;
import by.sorface.sso.web.records.responses.ProfileRecord;

import java.util.UUID;

public interface AccountFacade {

    ProfileRecord getCurrent(final UUID id);

    void update(final UUID id, UserPatchUpdate userPatchUpdate);

}
