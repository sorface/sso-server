package by.sorface.ssoserver.services.social;

import by.sorface.ssoserver.records.GitHubOAuth2User;
import by.sorface.ssoserver.services.SocialOAuth2UserService;
import by.sorface.ssoserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitHubOAuth2UserService extends AbstractSocialOAuth2UserService<GitHubOAuth2User>
        implements SocialOAuth2UserService<GitHubOAuth2User> {

    @Autowired
    public GitHubOAuth2UserService(final UserService userService) {
        super(userService);
    }

}
