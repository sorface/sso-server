package by.sorface.ssoserver.records;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

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
        final var avatarUrl = String.valueOf("%s".formatted(oAuth2User.getAttributes().get("avatar_url")));
        final var email = String.valueOf(oAuth2User.getAttributes().get("default_email"));

        return YandexOAuth2User.builder()
                .id(id)
                .avatarUrl(avatarUrl)
                .username(login)
                .email(email)
                .build();
    }

}
