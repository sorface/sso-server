package by.sorface.sso.web.services.users.social;

import by.sorface.sso.web.dao.models.enums.ProviderType;
import by.sorface.sso.web.records.socialusers.GitHubOAuth2User;
import by.sorface.sso.web.services.users.RoleService;
import by.sorface.sso.web.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitHubOAuth2UserService extends AbstractSocialOAuth2UserService<GitHubOAuth2User>
        implements SocialOAuth2UserService<GitHubOAuth2User> {

    @Autowired
    public GitHubOAuth2UserService(final UserService userService,
                                   final RoleService roleService) {
        super(ProviderType.GITHUB, userService, roleService);
    }

}
