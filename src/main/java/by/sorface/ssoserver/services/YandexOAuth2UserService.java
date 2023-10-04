package by.sorface.ssoserver.services;

import by.sorface.ssoserver.records.YandexOAuth2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YandexOAuth2UserService extends AbstractSocialOAuth2UserService<YandexOAuth2User>
        implements SocialOAuth2UserService<YandexOAuth2User>{

    @Autowired
    protected YandexOAuth2UserService(UserService userService) {
        super(userService);
    }

}
