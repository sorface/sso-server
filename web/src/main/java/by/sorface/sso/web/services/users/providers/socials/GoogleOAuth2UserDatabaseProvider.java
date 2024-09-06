package by.sorface.sso.web.services.users.providers.socials;

import by.sorface.sso.web.converters.PrincipalConverter;
import by.sorface.sso.web.converters.socialusers.GoogleOAuth2UserConverter;
import by.sorface.sso.web.records.socialusers.GoogleOAuth2User;
import by.sorface.sso.web.services.users.providers.AbstractOAuth2UserDatabaseProvider;
import by.sorface.sso.web.services.users.social.GoogleOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleOAuth2UserDatabaseProvider extends AbstractOAuth2UserDatabaseProvider<GoogleOAuth2User> {

    @Autowired
    public GoogleOAuth2UserDatabaseProvider(final GoogleOAuth2UserService googleOAuth2UserService,
                                            final PrincipalConverter principalConverter,
                                            final GoogleOAuth2UserConverter googleOAuth2UserConverter) {
        super(googleOAuth2UserService, principalConverter, googleOAuth2UserConverter);
    }

}
