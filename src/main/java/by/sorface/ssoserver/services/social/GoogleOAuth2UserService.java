package by.sorface.ssoserver.services.social;

import by.sorface.ssoserver.records.GoogleOAuth2User;
import by.sorface.ssoserver.services.SocialOAuth2UserService;
import by.sorface.ssoserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleOAuth2UserService extends AbstractSocialOAuth2UserService<GoogleOAuth2User>
        implements SocialOAuth2UserService<GoogleOAuth2User> {

    @Autowired
    protected GoogleOAuth2UserService(final UserService userService) {
        super(userService);
    }

}
