package by.sorface.sso.web.facade.accounts;

import by.sorface.sso.web.dao.models.RoleEntity;
import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.exceptions.NotFoundException;
import by.sorface.sso.web.records.requests.UserPatchUpdate;
import by.sorface.sso.web.records.responses.ProfileRecord;
import by.sorface.sso.web.services.users.UserService;
import by.sorface.sso.web.utils.NullUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultAccountFacade implements AccountFacade {

    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public ProfileRecord getCurrent(final UUID id) {
        final UserEntity user = userService.findById(id);

        if (Objects.isNull(user)) {
            throw new NotFoundException("application.user.not-found-by-id", Map.of("id", id.toString()));
        }

        return new ProfileRecord(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAvatarUrl(),
                user.getRoles().stream().map(RoleEntity::getValue).toList());
    }


    @Override
    public void update(final UUID id, final UserPatchUpdate userPatchUpdate) {
        final UserEntity user = userService.findById(id);

        if (Objects.isNull(user)) {
            throw new NotFoundException("application.user.not-found-by-id", Map.of("id", id.toString()));
        }

        NullUtils.setIfNonNull(userPatchUpdate.firstname(), user::setFirstName);
        NullUtils.setIfNonNull(userPatchUpdate.lastname(), user::setLastName);

        userService.save(user);
    }

}
