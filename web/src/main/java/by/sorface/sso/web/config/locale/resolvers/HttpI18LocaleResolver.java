package by.sorface.sso.web.config.locale.resolvers;

import by.sorface.sso.web.constants.SupportedLocales;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HttpI18LocaleResolver extends AcceptHeaderLocaleResolver {

    @SuppressWarnings("NullableProblems")
    @Override
    public Locale resolveLocale(final HttpServletRequest request) {
        final Locale locale = getLocale(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication) && !(authentication instanceof AnonymousAuthenticationToken)) {
            RequestContextHolder.currentRequestAttributes().setAttribute("SECURITY_LOCALE", locale, RequestAttributes.SCOPE_SESSION);
        }

        return locale;
    }

    private Locale getLocale(final HttpServletRequest request) {
        if (StringUtils.hasLength(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE))) {
            return SupportedLocales.EN;
        }

        final List<Locale.LanguageRange> list = Locale.LanguageRange.parse(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE));

        return Locale.lookup(list, List.of(SupportedLocales.EN, SupportedLocales.RU));
    }
}
