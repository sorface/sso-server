package by.sorface.sso.web.config.handlers;

import by.sorface.sso.web.config.properties.MvcEndpointProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SavedRequestRedisSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String SORFACE_NEXT_LOCATION = "Sorface-Next-Location";

    private final MvcEndpointProperties mvcEndpointProperties;

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        final var savedRequest = this.requestCache.getRequest(request, response);

        if (savedRequest == null) {
            response.setHeader(SORFACE_NEXT_LOCATION, mvcEndpointProperties.getUriPageProfile());

            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }

        String targetUrlParameter = getTargetUrlParameter();

        if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            this.requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }
        clearAuthenticationAttributes(request);
        // Use the DefaultSavedRequest URL
        String targetUrl = savedRequest.getRedirectUrl();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }

}
