package by.sorface.sso.web.services.users.social;

import by.sorface.sso.web.dao.models.RoleEntity;
import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.records.socialusers.SocialOAuth2User;
import by.sorface.sso.web.services.users.RoleService;
import by.sorface.sso.web.services.users.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

public abstract class AbstractSocialOAuth2UserService<T extends SocialOAuth2User> implements SocialOAuth2UserService<T> {

    private static final String DEFAULT_ROLE_USER = "User";

    private final UserService userService;

    private final RoleService roleService;

    protected AbstractSocialOAuth2UserService(final UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public UserEntity findOrCreate(final T socialOAuth2User) {
        final UserEntity userByEmail = this.findAndUpdateByEmail(socialOAuth2User);

        if (Objects.nonNull(userByEmail)) {
            return userByEmail;
        }

        final UserEntity newUser = this.createNewUser(socialOAuth2User);

        final RoleEntity userRole = roleService.findByValue(DEFAULT_ROLE_USER);
        newUser.setRoles(List.of(userRole));

        return userService.save(newUser);
    }

    protected UserEntity findAndUpdateByEmail(final T socialOAuth2User) {
        final UserEntity user = userService.findByEmail(socialOAuth2User.getEmail());

        if (Objects.nonNull(user)) {
            if (Objects.isNull(user.getUsername())) {
                user.setUsername(socialOAuth2User.getUsername());
            }

            if (Objects.isNull(user.getAvatarUrl())) {
                user.setAvatarUrl(socialOAuth2User.getAvatarUrl());
            }
        }

        return user;
    }

    protected UserEntity createNewUser(final T socialOAuth2User) {
        final var newUser = new UserEntity();
        {
            newUser.setEmail(socialOAuth2User.getEmail());
            newUser.setUsername(socialOAuth2User.getUsername());
            newUser.setAvatarUrl(socialOAuth2User.getAvatarUrl());
            newUser.setLastName(socialOAuth2User.getLastName());
            newUser.setFirstName(socialOAuth2User.getFirstName());
            newUser.setMiddleName(socialOAuth2User.getMiddleName());
        }

        return newUser;
    }

}
