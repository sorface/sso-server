package by.sorface.sso.web.services.providers.socials;

import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.records.principals.SfPrincipal;
import by.sorface.sso.web.records.socialusers.GitHubOAuth2User;
import by.sorface.sso.web.services.providers.AbstractOAuth2UserDatabaseProvider;
import by.sorface.sso.web.services.users.social.SocialOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class GitHubOAuth2UserDatabaseProvider extends AbstractOAuth2UserDatabaseProvider<GitHubOAuth2User> {

    @Autowired
    public GitHubOAuth2UserDatabaseProvider(final SocialOAuth2UserService<GitHubOAuth2User> oAuth2UserSocialOAuth2UserService,
                                            @Qualifier("defaultSfUserMapper") final Converter<UserEntity, SfPrincipal> principalConverter) {
        super(oAuth2UserSocialOAuth2UserService, principalConverter);
    }

    @Override
    protected GitHubOAuth2User buildOAuth2User(OAuth2User oAuth2User) {
        return GitHubOAuth2User.from(oAuth2User);
    }

}
