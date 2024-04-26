package by.sorface.sso.web.config.security.cookies;

import by.sorface.sso.web.config.options.CookieOptions;
import by.sorface.sso.web.utils.json.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Slf4j
@Configuration
public class CookieConfiguration {

    @Bean
    public CookieSerializer cookieSerializer(final CookieOptions cookieOptions) {
        log.debug("config default session cookie");

        final CookieOptions.SessionCookieOptions session = cookieOptions.getSession();

        final var serializer = new DefaultCookieSerializer();
        {
            serializer.setCookieName(session.getName());
            serializer.setCookiePath(session.getPath());
            serializer.setDomainNamePattern(session.getDomainPattern());
            serializer.setUseHttpOnlyCookie(session.isHttpOnly());
            serializer.setSameSite(session.getSameSite().getValue());
        }

        log.debug("session cookie configured {}{}", System.lineSeparator(), Json.lazyStringify(session));

        return serializer;
    }

}
