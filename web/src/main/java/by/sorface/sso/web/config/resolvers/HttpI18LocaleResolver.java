package by.sorface.sso.web.config.resolvers;

import by.sorface.sso.web.constants.SupportedLocales;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

public class HttpI18LocaleResolver extends AcceptHeaderLocaleResolver {

    @SuppressWarnings("NullableProblems")
    @Override
    public Locale resolveLocale(final HttpServletRequest request) {
        if (StringUtils.hasLength(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE))) {
            return SupportedLocales.EN;
        }

        final List<Locale.LanguageRange> list = Locale.LanguageRange.parse(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE));

        return Locale.lookup(list, List.of(SupportedLocales.EN, SupportedLocales.RU));
    }

}
