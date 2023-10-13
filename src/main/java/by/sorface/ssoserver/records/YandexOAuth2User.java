package by.sorface.ssoserver.records;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Objects;

@Getter
@Builder
public class YandexOAuth2User implements SocialOAuth2User {

    private String id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String middleName;

    private String avatarUrl;

    private  Map<String, Object> attributes;

    public static YandexOAuth2User build(final OAuth2User oAuth2User) {
        final var id = String.valueOf(oAuth2User.getAttributes().get("id"));
        final var login = String.valueOf(oAuth2User.getAttributes().get("login"));
        final Object avatarUrlObject = oAuth2User.getAttributes().get("avatar_url");

        YandexOAuth2UserBuilder yandexOAuth2UserBuilder = YandexOAuth2User.builder()
                .id(id)
                .username(login);

        if (Objects.nonNull(avatarUrlObject)) {
            yandexOAuth2UserBuilder.avatarUrl(String.valueOf("%s".formatted(avatarUrlObject)));
        }

        final var email = String.valueOf(oAuth2User.getAttributes().get("default_email"));
        yandexOAuth2UserBuilder.email(email);

        return yandexOAuth2UserBuilder.build();
    }

}
