package by.sorface.ssoserver.services.providers;

import by.sorface.ssoserver.constants.enums.OAuthProvider;
import by.sorface.ssoserver.mappers.SorfaceUserMapper;
import by.sorface.ssoserver.records.GitHubOAuth2User;
import by.sorface.ssoserver.records.GoogleOAuth2User;
import by.sorface.ssoserver.records.YandexOAuth2User;
import by.sorface.ssoserver.services.SocialOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuth2UserDatabaseProvider extends DefaultOAuth2UserService {

    private final SocialOAuth2UserService<GitHubOAuth2User> gitHubSocialOAuth2UserServiceImpl;

    private final SocialOAuth2UserService<GoogleOAuth2User> googleOAuth2UserSocialOAuth2UserService;

    private final SocialOAuth2UserService<YandexOAuth2User> yandexOAuth2UserSocialOAuth2UserService;

    private final SorfaceUserMapper sorfaceUserMapper;

    @Autowired
    public OAuth2UserDatabaseProvider(SocialOAuth2UserService<GitHubOAuth2User> gitHubSocialOAuth2UserServiceImpl,
                                      SocialOAuth2UserService<GoogleOAuth2User> googleOAuth2UserSocialOAuth2UserService,
                                      SocialOAuth2UserService<YandexOAuth2User> yandexOAuth2UserSocialOAuth2UserService,
                                      SorfaceUserMapper sorfaceUserMapper) {
        this.gitHubSocialOAuth2UserServiceImpl = gitHubSocialOAuth2UserServiceImpl;
        this.googleOAuth2UserSocialOAuth2UserService = googleOAuth2UserSocialOAuth2UserService;
        this.yandexOAuth2UserSocialOAuth2UserService = yandexOAuth2UserSocialOAuth2UserService;
        this.sorfaceUserMapper = sorfaceUserMapper;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User oAuth2User = super.loadUser(userRequest);

        final String providerName = userRequest.getClientRegistration().getClientName();

        final var provider = OAuthProvider.findByName(providerName);

        final var userEntity = switch (provider) {
            case GITHUB -> {
                final var gitHubOAuth2User = GitHubOAuth2User.build(oAuth2User);

                yield gitHubSocialOAuth2UserServiceImpl.findOrCreate(gitHubOAuth2User);
            }
            case GOOGLE -> {
                final var googleOAuth2User = GoogleOAuth2User.build(oAuth2User);

                yield googleOAuth2UserSocialOAuth2UserService.findOrCreate(googleOAuth2User);
            }
            case YANDEX -> {
                final var yandexOAuth2User = YandexOAuth2User.build(oAuth2User);

                yield yandexOAuth2UserSocialOAuth2UserService.findOrCreate(yandexOAuth2User);
            }

            case UNKNOWN -> throw new OAuth2AuthenticationException("Provider %s not supported".formatted(providerName));
        };

        return sorfaceUserMapper.to(userEntity);
    }

}
