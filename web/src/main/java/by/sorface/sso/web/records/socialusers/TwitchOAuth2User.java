package by.sorface.sso.web.records.socialusers;

import by.sorface.sso.web.exceptions.ObjectInvalidException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Map;

@Getter
@Builder
public class TwitchOAuth2User implements SocialOAuth2User {

    private String id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String middleName;

    private String avatarUrl;

    private Map<String, Object> attributes;

    @SuppressWarnings("unchecked")
    public static TwitchOAuth2User from(final OAuth2User oAuth2User) {
        final var user = ((ArrayList<Map<String, String>>) oAuth2User.getAttributes().get("data"))
                .stream()
                .findFirst()
                .orElseThrow(() -> new ObjectInvalidException("twitch claims invalid"));

        final var id = String.valueOf(user.get("id"));
        final var login = String.valueOf(user.get("login"));
        final var avatarUrl = String.valueOf(user.get("profile_image_url"));
        final var email = String.valueOf(user.get("email"));

        return TwitchOAuth2User.builder()
                .id(id)
                .avatarUrl(avatarUrl)
                .username(login)
                .email(email)
                .build();
    }

}
