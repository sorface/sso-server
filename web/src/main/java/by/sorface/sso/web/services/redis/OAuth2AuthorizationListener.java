package by.sorface.sso.web.services.redis;

import by.sorface.sso.web.services.redis.events.OAuth2AuthorizationCompleteEvent;
import by.sorface.sso.web.services.redis.events.OAuth2AuthorizationDeleteEvent;

public interface OAuth2AuthorizationListener {

    void complete(final OAuth2AuthorizationCompleteEvent event);

    void delete(final OAuth2AuthorizationDeleteEvent event);

}
