package by.sorface.ssoserver.services;

import by.sorface.ssoserver.records.GoogleOAuth2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleOAuth2UserService extends AbstractSocialOAuth2UserService<GoogleOAuth2User>
        implements SocialOAuth2UserService<GoogleOAuth2User> {

    @Autowired
    protected GoogleOAuth2UserService(UserService userService) {
        super(userService);
    }

}
