package by.sorface.sso.web.utils;

import by.sorface.sso.web.records.principals.DefaultPrincipal;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

import javax.annotation.Nullable;
import java.util.Objects;

import static by.sorface.sso.web.utils.LogicUtils.not;


@UtilityClass
public class OAuth2AuthorizationUtils {

    private final static String PRINCIPAL_ATTRIBUTE_KEY = "java.security.Principal";

    public boolean hasPrincipal(final OAuth2Authorization authentication) {
        return authentication.getAttributes().containsKey(PRINCIPAL_ATTRIBUTE_KEY);
    }

    @Nullable
    public DefaultPrincipal getPrincipal(final OAuth2Authorization authentication) {
        final boolean containsPrincipalAuthentication = hasPrincipal(authentication);

        if (not(containsPrincipalAuthentication)) {
            return null;
        }

        final Authentication authenticationPrincipal = authentication.getAttribute(PRINCIPAL_ATTRIBUTE_KEY);

        if (Objects.isNull(authenticationPrincipal)) {
            return null;
        }

        final Object principalObject = authenticationPrincipal.getPrincipal();

        if (not(principalObject instanceof DefaultPrincipal)) {
            return null;
        }

        return (DefaultPrincipal) principalObject;
    }

}
