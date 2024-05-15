package by.sorface.sso.web.config.locale.resolvers;

import by.sorface.sso.web.constants.SessionAttributes;
import by.sorface.sso.web.constants.SupportedLocales;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class HttpI18LocaleResolver extends AcceptHeaderLocaleResolver {

    /**
     * The resolveLocale function is called by Spring to determine the locale of the current request.
     * The function first checks if a Locale has been set in the session, and returns that if it exists.
     * If not, it then checks for a cookie named &quot;locale&quot; and returns its value as a Locale object.
     * If neither of these are present, it falls back on returning null (which will cause Spring to use its default behavior).
     *
     * @param request Get the locale from the request
     * @return The locale of the current request
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public Locale resolveLocale(final HttpServletRequest request) {
        final Locale locale = getLocale(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication) && !(authentication instanceof AnonymousAuthenticationToken)) {
            RequestContextHolder.currentRequestAttributes().setAttribute(SessionAttributes.USER_LOCALE, locale, RequestAttributes.SCOPE_SESSION);
        }

        return locale;
    }

    /**
     * The getLocale function returns the locale of the user's browser.
     * If there is no Accept-Language header, it defaults to English.
     * Otherwise, it uses Java's Locale class to parse and lookup the language ranges in order of preference.
     *
     * @param request Get the header of the request
     * @return The locale that is supported by the application
     */
    private Locale getLocale(final HttpServletRequest request) {
        String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);

        if (!StringUtils.hasLength(language)) {
            return SupportedLocales.EN;
        }

        log.info("setup language for user: {}", language);

        final List<Locale.LanguageRange> list = Locale.LanguageRange.parse(language);

        return Locale.lookup(list, List.of(SupportedLocales.EN, SupportedLocales.RU));
    }

}
