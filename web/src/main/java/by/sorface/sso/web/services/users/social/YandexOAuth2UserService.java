package by.sorface.sso.web.services.users.social;

import by.sorface.sso.web.records.YandexOAuth2User;
import by.sorface.sso.web.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YandexOAuth2UserService extends AbstractSocialOAuth2UserService<YandexOAuth2User>
        implements SocialOAuth2UserService<YandexOAuth2User> {

    @Autowired
    protected YandexOAuth2UserService(final UserService userService) {
        super(userService);
    }

}
