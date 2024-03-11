package by.sorface.sso.web.converters;

import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.records.principals.DefaultPrincipal;
import org.springframework.core.convert.converter.Converter;

public interface PrincipalConverter extends Converter<UserEntity, DefaultPrincipal> {
}
