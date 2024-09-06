package by.sorface.sso.web.services.users.social;

import by.sorface.sso.web.dao.models.enums.ProviderType;
import by.sorface.sso.web.records.socialusers.TwitchOAuth2User;
import by.sorface.sso.web.services.users.RoleService;
import by.sorface.sso.web.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwitchOAuth2UserService extends AbstractSocialOAuth2UserService<TwitchOAuth2User>
        implements SocialOAuth2UserService<TwitchOAuth2User> {

    @Autowired
    public TwitchOAuth2UserService(final UserService userService,
                                   final RoleService roleService) {
        super(ProviderType.TWITCH, userService, roleService);
    }

}
