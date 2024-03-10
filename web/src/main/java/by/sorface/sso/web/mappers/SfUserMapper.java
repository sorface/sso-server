package by.sorface.sso.web.mappers;

import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.records.principals.SfPrincipal;
import org.springframework.core.convert.converter.Converter;

public interface SfUserMapper extends Converter<UserEntity, SfPrincipal> {
}
