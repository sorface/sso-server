package by.sorface.sso.web.converters.socialusers;

import by.sorface.sso.web.constants.claims.GitHubClaims;
import by.sorface.sso.web.records.socialusers.GitHubOAuth2User;
import by.sorface.sso.web.utils.UserUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GitHubOAuth2UserConverter implements OAuth2UserConverter<GitHubOAuth2User> {

    @Override
    public GitHubOAuth2User convert(final OAuth2User oAuth2User) {
        final var id = String.valueOf(oAuth2User.getAttributes().get(GitHubClaims.CLAIM_ID));
        final var avatarUrl = String.valueOf(oAuth2User.getAttributes().get(GitHubClaims.CLAIM_AVATAR_URL));
        final var login = String.valueOf(oAuth2User.getAttributes().get(GitHubClaims.CLAIM_LOGIN));
        final var name = String.valueOf(oAuth2User.getAttributes().get(GitHubClaims.CLAIM_NAME));
        final var email = String.valueOf(oAuth2User.getAttributes().get(GitHubClaims.CLAIM_EMAIL));

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
