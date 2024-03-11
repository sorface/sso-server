package by.sorface.sso.web.converters.socialusers;

import by.sorface.sso.web.constants.claims.YandexClaims;
import by.sorface.sso.web.records.socialusers.YandexOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class YandexOAuth2UserConverter implements OAuth2UserConverter<YandexOAuth2User> {

    @Override
    public YandexOAuth2User convert(final OAuth2User oAuth2User) {
        final var id = String.valueOf(oAuth2User.getAttributes().get(YandexClaims.CLAIM_ID));
        final var login = String.valueOf(oAuth2User.getAttributes().get(YandexClaims.CLAIM_LOGIN));
        final Object avatarUrlObject = oAuth2User.getAttributes().get(YandexClaims.CLAIM_AVATAR_URL);

        final var yandexOAuth2UserBuilder = YandexOAuth2User.builder()
                .id(id)
                .username(login);

        if (Objects.nonNull(avatarUrlObject)) {
            yandexOAuth2UserBuilder.avatarUrl(String.valueOf(avatarUrlObject));
        }

        final var email = String.valueOf(oAuth2User.getAttributes().get(YandexClaims.CLAIM_DEFAULT_EMAIL));
        yandexOAuth2UserBuilder.email(email);

        return yandexOAuth2UserBuilder.build();
    }

}
