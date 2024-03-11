package by.sorface.sso.web.services.providers.socials;

import by.sorface.sso.web.converters.PrincipalConverter;
import by.sorface.sso.web.converters.socialusers.GitHubOAuth2UserConverter;
import by.sorface.sso.web.records.socialusers.GitHubOAuth2User;
import by.sorface.sso.web.services.providers.AbstractOAuth2UserDatabaseProvider;
import by.sorface.sso.web.services.users.social.GitHubOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitHubOAuth2UserDatabaseProvider extends AbstractOAuth2UserDatabaseProvider<GitHubOAuth2User> {

    @Autowired
    public GitHubOAuth2UserDatabaseProvider(final GitHubOAuth2UserService gitHubOAuth2UserService,
                                            final PrincipalConverter principalConverter,
                                            final GitHubOAuth2UserConverter gitHubOAuth2UserConverter) {
        super(gitHubOAuth2UserService, principalConverter, gitHubOAuth2UserConverter);
    }

}
