package by.sorface.sso.web.services.providers.socials;

import by.sorface.sso.web.converters.OAuth2TwitchUserRequestEntityConverter;
import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.records.principals.SfPrincipal;
import by.sorface.sso.web.records.socialusers.TwitchOAuth2User;
import by.sorface.sso.web.services.providers.AbstractOAuth2UserDatabaseProvider;
import by.sorface.sso.web.services.users.social.SocialOAuth2UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class TwitchOAuth2UserDatabaseProvider extends AbstractOAuth2UserDatabaseProvider<TwitchOAuth2User> {

    public static final OAuth2TwitchUserRequestEntityConverter TWITCH_REQUEST_CONVERTER;

    static {
        TWITCH_REQUEST_CONVERTER = new OAuth2TwitchUserRequestEntityConverter();
    }

    @Autowired
    public TwitchOAuth2UserDatabaseProvider(final SocialOAuth2UserService<TwitchOAuth2User> oAuth2UserSocialOAuth2UserService,
                                            @Qualifier("defaultSfUserMapper") final Converter<UserEntity, SfPrincipal> principalConverter) {
        super(oAuth2UserSocialOAuth2UserService, principalConverter);
    }

    @PostConstruct
    void init() {
        super.setRequestEntityConverter(TWITCH_REQUEST_CONVERTER);
    }

    @Override
    protected TwitchOAuth2User buildOAuth2User(OAuth2User oAuth2User) {
        return TwitchOAuth2User.from(oAuth2User);
    }

}
