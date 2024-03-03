package by.sorface.sso.web.services.providers;

import by.sorface.sso.web.constants.enums.OAuthProvider;
import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.mappers.SfUserMapper;
import by.sorface.sso.web.records.GitHubOAuth2User;
import by.sorface.sso.web.records.YandexOAuth2User;
import by.sorface.sso.web.services.users.social.SocialOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис получения пользователя из сторонних севисов
 */
@Service
public class OAuth2UserDatabaseProvider extends DefaultOAuth2UserService {

    private final SocialOAuth2UserService<GitHubOAuth2User> gitHubSocialOAuth2UserServiceImpl;

    private final SocialOAuth2UserService<YandexOAuth2User> yandexOAuth2UserSocialOAuth2UserService;

    private final SfUserMapper sfUserMapper;

    @Autowired
    public OAuth2UserDatabaseProvider(SocialOAuth2UserService<GitHubOAuth2User> gitHubSocialOAuth2UserServiceImpl,
                                      SocialOAuth2UserService<YandexOAuth2User> yandexOAuth2UserSocialOAuth2UserService,
                                      SfUserMapper sfUserMapper) {
        this.gitHubSocialOAuth2UserServiceImpl = gitHubSocialOAuth2UserServiceImpl;
        this.yandexOAuth2UserSocialOAuth2UserService = yandexOAuth2UserSocialOAuth2UserService;
        this.sfUserMapper = sfUserMapper;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User oAuth2User = super.loadUser(userRequest);

        final String providerName = userRequest.getClientRegistration().getClientName();

        final var provider = OAuthProvider.findByName(providerName);

        final var userEntity = getSorfaceUserByProvider(oAuth2User, provider);

        return sfUserMapper.to(userEntity);
    }

    /**
     * Получение пользователя из базы дынных
     *
     * @param oAuth2User    пользователь стороннего
     * @param oAuthProvider тип провайдера
     * @return sorface пользователь
     */
    private UserEntity getSorfaceUserByProvider(final OAuth2User oAuth2User, final OAuthProvider oAuthProvider) {
        return switch (oAuthProvider) {
            case GITHUB -> {
                final var gitHubOAuth2User = GitHubOAuth2User.parse(oAuth2User);

                yield gitHubSocialOAuth2UserServiceImpl.findOrCreate(gitHubOAuth2User);
            }
            case YANDEX -> {
                final var yandexOAuth2User = YandexOAuth2User.parse(oAuth2User);

                yield yandexOAuth2UserSocialOAuth2UserService.findOrCreate(yandexOAuth2User);
            }

            default -> throw new OAuth2AuthenticationException("Provider %s not supported".formatted(oAuthProvider));
        };
    }

}
