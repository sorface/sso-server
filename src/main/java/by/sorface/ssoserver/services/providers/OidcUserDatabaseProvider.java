package by.sorface.ssoserver.services.providers;

import by.sorface.ssoserver.constants.enums.OAuthProvider;
import by.sorface.ssoserver.dao.models.UserEntity;
import by.sorface.ssoserver.mappers.SorfaceUserMapper;
import by.sorface.ssoserver.records.GoogleOAuth2User;
import by.sorface.ssoserver.services.SocialOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OidcUserDatabaseProvider extends OidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final SocialOAuth2UserService<GoogleOAuth2User> googleOAuth2UserSocialOAuth2UserService;

    private final SorfaceUserMapper sorfaceUserMapper;

    @Autowired
    public OidcUserDatabaseProvider(final SocialOAuth2UserService<GoogleOAuth2User> googleOAuth2UserSocialOAuth2UserService,
                                    final SorfaceUserMapper sorfaceUserMapper) {
        this.googleOAuth2UserSocialOAuth2UserService = googleOAuth2UserSocialOAuth2UserService;
        this.sorfaceUserMapper = sorfaceUserMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User oAuth2User = super.loadUser(userRequest);

        final String clientName = userRequest.getClientRegistration().getClientName();

        final var provider = OAuthProvider.findByName(clientName);

        UserEntity sorfaceUserByProvider = getSorfaceUserByProvider(oAuth2User, provider);

        return sorfaceUserMapper.to(sorfaceUserByProvider);
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    private UserEntity getSorfaceUserByProvider(final OAuth2User oAuth2User, final OAuthProvider oAuthProvider) {
        return switch (oAuthProvider) {
            case GOOGLE -> {
                final var googleOAuth2User = GoogleOAuth2User.parse(oAuth2User);

                yield googleOAuth2UserSocialOAuth2UserService.findOrCreate(googleOAuth2User);
            }
            default -> throw new OAuth2AuthenticationException("Provider %s not supported".formatted(oAuthProvider));
        };
    }

}
