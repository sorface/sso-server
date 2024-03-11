package by.sorface.sso.web.converters;

import by.sorface.sso.web.dao.models.RoleEntity;
import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.records.principals.DefaultPrincipal;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DefaultPrincipalConverter implements PrincipalConverter {

    @Override
    public DefaultPrincipal convert(final UserEntity user) {
        final List<GrantedAuthority> authorities = this.convertRoles(user.getRoles());

        final String userPassword = Optional.ofNullable(user.getPassword()).orElse(Strings.EMPTY);

        final var sorfaceUser = new DefaultPrincipal(user.getUsername(), userPassword, true, authorities);
        {
            sorfaceUser.setId(user.getId());
            sorfaceUser.setFirstName(user.getFirstName());
            sorfaceUser.setLastName(user.getLastName());
            sorfaceUser.setMiddleName(user.getMiddleName());
            sorfaceUser.setAvatarUrl(user.getAvatarUrl());
            sorfaceUser.setEmail(user.getEmail());
            sorfaceUser.setBirthday(user.getBirthday());
            sorfaceUser.setConfirm(user.isConfirm());
        }

        return sorfaceUser;
    }

    private List<GrantedAuthority> convertRoles(final Collection<? extends RoleEntity> roles) {
        return Optional.ofNullable(roles).stream()
                .flatMap(Collection::stream)
                .map(role -> new SimpleGrantedAuthority(role.getValue()))
                .collect(Collectors.toList());
    }

}
