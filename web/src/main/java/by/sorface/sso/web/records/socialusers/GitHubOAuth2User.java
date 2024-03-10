package by.sorface.sso.web.records.socialusers;

import by.sorface.sso.web.utils.UserUtils;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

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

    private Map<String, Object> attributes;

    public static GitHubOAuth2User from(final OAuth2User oAuth2User) {
        final var id = String.valueOf(oAuth2User.getAttributes().get("id"));
        final var avatarUrl = String.valueOf(oAuth2User.getAttributes().get("avatar_url"));
        final var login = String.valueOf(oAuth2User.getAttributes().get("login"));
        final var name = String.valueOf(oAuth2User.getAttributes().get("name"));
        final var email = String.valueOf(oAuth2User.getAttributes().get("email"));

        final var builder = GitHubOAuth2User.builder()
                .id(id)
                .avatarUrl(avatarUrl)
                .username(login)
                .email(email);

        if (Objects.nonNull(name)) {
            final UserUtils.DefaultFullName defaultFullName = UserUtils.parseFullName(name);

            builder
                    .firstName(defaultFullName.getFirstName())
                    .lastName(defaultFullName.getLastName())
                    .middleName(defaultFullName.getOtherName());
        }

        return builder.build();
    }

}
