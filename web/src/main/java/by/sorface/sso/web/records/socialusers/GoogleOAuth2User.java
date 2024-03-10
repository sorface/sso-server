package by.sorface.sso.web.records.socialusers;

import by.sorface.sso.web.utils.UserUtils;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Objects;

@Getter
@Builder
public class GoogleOAuth2User implements SocialOAuth2User {

    public static final String ATTRIBUTE_SUB = "sub";
    public static final String ATTRIBUTE_PICTURE = "picture";
    public static final String ATTRIBUTE_EMAIL = "email";
    public static final String ATTRIBUTE_NAME = "name";
    private String id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String middleName;

    private String avatarUrl;

    private Map<String, Object> attributes;

    public static GoogleOAuth2User from(final OAuth2User oAuth2User) {
        final var id = String.valueOf(oAuth2User.getAttributes().get(ATTRIBUTE_SUB));
        final var avatarUrl = String.valueOf(oAuth2User.getAttributes().get(ATTRIBUTE_PICTURE));
        final var name = String.valueOf(oAuth2User.getAttributes().get(ATTRIBUTE_NAME));
        final var email = String.valueOf(oAuth2User.getAttributes().get(ATTRIBUTE_EMAIL));

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
