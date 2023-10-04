package by.sorface.ssoserver.services;

import by.sorface.ssoserver.dao.models.UserEntity;
import by.sorface.ssoserver.records.SocialOAuth2User;

public interface SocialOAuth2UserService<T extends SocialOAuth2User> {

    UserEntity findOrCreate(final T oAuth2user);

}
