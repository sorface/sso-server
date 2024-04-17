package by.sorface.sso.web.config.security.csrf;

import by.sorface.sso.web.utils.json.Json;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {

    private final CsrfTokenRequestHandler delegate;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
        this.delegate.handle(request, response, csrfToken);
    }

    @Override
    public String resolveCsrfTokenValue(final HttpServletRequest request, final CsrfToken csrfToken) {
        log.info("csrf token [{}] resolved", Json.lazyStringifyWithMasking(csrfToken));

        if (StringUtils.hasText(request.getHeader(csrfToken.getHeaderName()))) {
            log.info("csrf token [{}] in header", request.getHeader(csrfToken.getHeaderName()));
            return super.resolveCsrfTokenValue(request, csrfToken);
        }

        return this.delegate.resolveCsrfTokenValue(request, csrfToken);
    }
}
