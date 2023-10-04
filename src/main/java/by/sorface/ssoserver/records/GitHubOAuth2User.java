package by.sorface.ssoserver.records;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Getter
@Builder
public class GitHubOAuth2User implements SocialOAuth2User {

    private String id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String middleName;

    private String avatarUrl;

    private  Map<String, Object> attributes;

    public static GitHubOAuth2User build(final OAuth2User oAuth2User) {
        final var id = String.valueOf(oAuth2User.getAttributes().get("id"));
        final var avatarUrl = String.valueOf(oAuth2User.getAttributes().get("avatar_url"));
        final var login = String.valueOf(oAuth2User.getAttributes().get("login"));
        final var name = String.valueOf(oAuth2User.getAttributes().get("name"));
        final var email = String.valueOf(oAuth2User.getAttributes().get("email"));

        final GitHubOAuth2User.GitHubOAuth2UserBuilder builder = GitHubOAuth2User.builder()
                .id(id)
                .avatarUrl(avatarUrl)
                .username(login)
                .email(email);

        if (Objects.nonNull(name)) {
            final var strings = Arrays.stream(name.split(" "))
                    .map(String::trim)
                    .filter(it -> it.length() > 0)
                    .toArray(String[]::new);

            if (strings.length > 1) {
                builder.firstName(strings[0]);
            }

            if (strings.length >= 2) {
                builder.lastName(strings[1]);
            }

            if (strings.length >= 3) {
                builder.middleName(strings[2]);
            }
        }

        return builder.build();
    }

}
