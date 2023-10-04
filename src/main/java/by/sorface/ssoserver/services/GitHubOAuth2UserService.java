package by.sorface.ssoserver.services;

import by.sorface.ssoserver.records.GitHubOAuth2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitHubOAuth2UserService extends AbstractSocialOAuth2UserService<GitHubOAuth2User>
        implements SocialOAuth2UserService<GitHubOAuth2User> {

    @Autowired
    public GitHubOAuth2UserService(UserService userService) {
        super(userService);
    }

}
