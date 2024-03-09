package by.sorface.sso.web.services.providers;

import by.sorface.sso.web.constants.OAuthProvider;
import by.sorface.sso.web.converters.OAuth2TwitchUserRequestEntityConverter;
import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.mappers.SfUserMapper;
import by.sorface.sso.web.records.GitHubOAuth2User;
import by.sorface.sso.web.records.GoogleOAuth2User;
import by.sorface.sso.web.records.TwitchOAuth2User;
import by.sorface.sso.web.records.YandexOAuth2User;
import by.sorface.sso.web.services.users.social.SocialOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Service for getting a user from third-party services
 */
@Service
public class OAuth2UserDatabaseProvider extends DefaultOAuth2UserService {

    private static final Converter<OAuth2UserRequest, RequestEntity<?>> REQUEST_ENTITY_CONVERTER = new OAuth2UserRequestEntityConverter();

    private final SocialOAuth2UserService<GitHubOAuth2User> gitHubSocialOAuth2UserServiceImpl;

    private final SocialOAuth2UserService<YandexOAuth2User> yandexOAuth2UserSocialOAuth2UserService;

    private final SocialOAuth2UserService<GoogleOAuth2User> googleOAuth2UserSocialOAuth2UserService;

    private final SocialOAuth2UserService<TwitchOAuth2User> twitchOAuth2UserSocialOAuth2UserService;

    private final SfUserMapper sfUserMapper;

    @Autowired
    public OAuth2UserDatabaseProvider(SocialOAuth2UserService<GitHubOAuth2User> gitHubSocialOAuth2UserServiceImpl,
                                      SocialOAuth2UserService<YandexOAuth2User> yandexOAuth2UserSocialOAuth2UserService,
                                      SocialOAuth2UserService<TwitchOAuth2User> twitchOAuth2UserSocialOAuth2UserService,
                                      SocialOAuth2UserService<GoogleOAuth2User> googleOAuth2UserSocialOAuth2UserService,
                                      SfUserMapper sfUserMapper) {
        this.gitHubSocialOAuth2UserServiceImpl = gitHubSocialOAuth2UserServiceImpl;
        this.yandexOAuth2UserSocialOAuth2UserService = yandexOAuth2UserSocialOAuth2UserService;
        this.googleOAuth2UserSocialOAuth2UserService = googleOAuth2UserSocialOAuth2UserService;
        this.twitchOAuth2UserSocialOAuth2UserService = twitchOAuth2UserSocialOAuth2UserService;
        this.sfUserMapper = sfUserMapper;
    }

    /**
     * Getting user data from third-party services
     *
     * @param userRequest the user request
     * @return пользователь oauth 2.0
     * @throws OAuth2AuthenticationException error getting the user
     */
    @Override
    @Transactional
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final var provider = getAuthProvider(userRequest);

        setRequestConverter(provider);

        final OAuth2User oAuth2User = super.loadUser(userRequest);

        final var userEntity = this.getOrCreate(oAuth2User, provider);

        return sfUserMapper.to(userEntity);
    }

    private void setRequestConverter(final OAuthProvider provider) {
        final var converter = switch (provider) {
            case TWITCH -> new OAuth2TwitchUserRequestEntityConverter();
            default -> null;
        };

        if (Objects.nonNull(converter)) {
            setRequestEntityConverter(converter);
        }
    }

    private OAuthProvider getAuthProvider(final OAuth2UserRequest oAuth2UserRequest) {
        final String providerName = oAuth2UserRequest.getClientRegistration().getClientName();

        return OAuthProvider.findByName(providerName);
    }

    /**
     * Получение пользователя из базы дынных
     *
     * @param oAuth2User    пользователь стороннего
     * @param oAuthProvider тип провайдера
     * @return sorface пользователь
     */
    private UserEntity getOrCreate(final OAuth2User oAuth2User, final OAuthProvider oAuthProvider) {
        return switch (oAuthProvider) {
            case GITHUB -> {
                setRequestEntityConverter(new OAuth2UserRequestEntityConverter());
                final var gitHubOAuth2User = GitHubOAuth2User.parse(oAuth2User);

                yield gitHubSocialOAuth2UserServiceImpl.findOrCreate(gitHubOAuth2User);
            }
            case YANDEX -> {
                final var yandexOAuth2User = YandexOAuth2User.parse(oAuth2User);

                yield yandexOAuth2UserSocialOAuth2UserService.findOrCreate(yandexOAuth2User);
            }
            case GOOGLE -> {
                final var googleOAuth2User = GoogleOAuth2User.parse(oAuth2User);

                yield googleOAuth2UserSocialOAuth2UserService.findOrCreate(googleOAuth2User);
            }
            case TWITCH -> {
                final var twitchOAuth2User = TwitchOAuth2User.parse(oAuth2User);

                yield twitchOAuth2UserSocialOAuth2UserService.findOrCreate(twitchOAuth2User);
            }

            default -> throw new OAuth2AuthenticationException("Provider %s not supported".formatted(oAuthProvider));
        };
    }

}
