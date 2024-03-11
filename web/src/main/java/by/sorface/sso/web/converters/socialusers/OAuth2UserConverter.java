package by.sorface.sso.web.converters.socialusers;

import by.sorface.sso.web.records.socialusers.SocialOAuth2User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserConverter<T extends SocialOAuth2User> extends Converter<OAuth2User, T> {
}
