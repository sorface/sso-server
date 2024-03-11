package by.sorface.sso.web.services.providers.socials;

import by.sorface.sso.web.converters.PrincipalConverter;
import by.sorface.sso.web.converters.socialusers.YandexOAuth2UserConverter;
import by.sorface.sso.web.records.socialusers.YandexOAuth2User;
import by.sorface.sso.web.services.providers.AbstractOAuth2UserDatabaseProvider;
import by.sorface.sso.web.services.users.social.YandexOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YandexOAuth2UserDatabaseProvider extends AbstractOAuth2UserDatabaseProvider<YandexOAuth2User> {

    @Autowired
    public YandexOAuth2UserDatabaseProvider(final YandexOAuth2UserService yandexOAuth2UserService,
                                            final PrincipalConverter principalConverter,
                                            final YandexOAuth2UserConverter yandexOAuth2UserConverter) {
        super(yandexOAuth2UserService, principalConverter, yandexOAuth2UserConverter);
    }

}
