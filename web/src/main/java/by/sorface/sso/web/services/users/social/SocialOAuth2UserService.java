package by.sorface.sso.web.services.users.social;

import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.records.SocialOAuth2User;

public interface SocialOAuth2UserService<T extends SocialOAuth2User> {

    UserEntity findOrCreate(final T oAuth2user);

}
