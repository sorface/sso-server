package by.sorface.sso.web.config.security.csrf;

import by.sorface.sso.web.config.options.CookieOptions;
import by.sorface.sso.web.utils.json.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CsrfConfiguration {

    @Bean
    public CookieCsrfTokenRepository cookieCsrfTokenRepository(final CookieOptions cookieOptions) {
        final var cookieCsrfTokenRepository = new CookieCsrfTokenRepository();

        final var csrfCookieOptions = cookieOptions.getCsrf();

        cookieCsrfTokenRepository.setCookiePath(csrfCookieOptions.getPath());
        cookieCsrfTokenRepository.setCookieName(csrfCookieOptions.getName());
        cookieCsrfTokenRepository.setCookieHttpOnly(false);
        cookieCsrfTokenRepository.setCookieDomain(csrfCookieOptions.getDomain());

        log.debug("configured CookieCsrfTokenRepository with settings {}{}", System.lineSeparator(), Json.lazyStringify(csrfCookieOptions));

        return cookieCsrfTokenRepository;
    }

    @Bean
    public CsrfTokenRequestHandler spaCsrfTokenRequestHandler() {
        return new SpaCsrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler());
    }

}
