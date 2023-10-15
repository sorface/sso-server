package by.sorface.ssoserver.services.social;

import by.sorface.ssoserver.records.YandexOAuth2User;
import by.sorface.ssoserver.services.SocialOAuth2UserService;
import by.sorface.ssoserver.services.UserService;
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
