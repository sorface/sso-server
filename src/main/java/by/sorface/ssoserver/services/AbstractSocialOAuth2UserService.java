package by.sorface.ssoserver.services;

import by.sorface.ssoserver.dao.models.UserEntity;
import by.sorface.ssoserver.records.SocialOAuth2User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

public abstract class AbstractSocialOAuth2UserService<T extends SocialOAuth2User> implements SocialOAuth2UserService<T> {

    private final UserService userService;

    protected AbstractSocialOAuth2UserService(final UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findOrCreate(final T socialOAuth2User) {
        final UserEntity userByEmail = this.findAndUpdateByEmail(socialOAuth2User);

        if (Objects.nonNull(userByEmail)) {
            return userByEmail;
        }

        final UserEntity newUser = this.createNewUser(socialOAuth2User);

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
        }

        return newUser;
    }

}
