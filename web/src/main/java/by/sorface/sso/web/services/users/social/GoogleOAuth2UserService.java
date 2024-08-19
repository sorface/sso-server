package by.sorface.sso.web.services.users.social;

import by.sorface.sso.web.records.socialusers.GoogleOAuth2User;
import by.sorface.sso.web.services.users.RoleService;
import by.sorface.sso.web.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleOAuth2UserService extends AbstractSocialOAuth2UserService<GoogleOAuth2User>
        implements SocialOAuth2UserService<GoogleOAuth2User> {

    @Autowired
    protected GoogleOAuth2UserService(final UserService userService,
                                      final RoleService roleService) {
        super(userService, roleService);
    }

}
