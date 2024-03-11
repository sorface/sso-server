package by.sorface.sso.web.converters.socialusers;

import by.sorface.sso.web.constants.claims.GoogleClaims;
import by.sorface.sso.web.records.socialusers.GoogleOAuth2User;
import by.sorface.sso.web.utils.UserUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GoogleOAuth2UserConverter implements OAuth2UserConverter<GoogleOAuth2User> {

    @Override
    public GoogleOAuth2User convert(final OAuth2User oAuth2User) {
        final var id = String.valueOf(oAuth2User.getAttributes().get(GoogleClaims.ATTRIBUTE_SUB));
        final var avatarUrl = String.valueOf(oAuth2User.getAttributes().get(GoogleClaims.ATTRIBUTE_PICTURE));
        final var name = String.valueOf(oAuth2User.getAttributes().get(GoogleClaims.ATTRIBUTE_NAME));
        final var email = String.valueOf(oAuth2User.getAttributes().get(GoogleClaims.ATTRIBUTE_EMAIL));

        final var builder = GoogleOAuth2User.builder()
                .id(id)
                .avatarUrl(avatarUrl)
                .username(email)
                .email(email);

        if (Objects.nonNull(name)) {
            UserUtils.DefaultFullName defaultFullName = UserUtils.parseFullName(name);

            builder
                    .firstName(defaultFullName.getFirstName())
                    .lastName(defaultFullName.getLastName())
                    .middleName(defaultFullName.getOtherName());
        }

        return builder.build();
    }

}
