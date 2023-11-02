package by.sorface.ssoserver.mappers;

import by.sorface.ssoserver.dao.models.RoleEntity;
import by.sorface.ssoserver.dao.models.UserEntity;
import by.sorface.ssoserver.records.SorfaceUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SorfaceUserMapperImpl implements SorfaceUserMapper {

    @Override
    public SorfaceUser to(final UserEntity user) {
        final List<GrantedAuthority> authorities = this.convertRoles(user.getRoles());

        final var sorfaceUser = new SorfaceUser(user.getUsername(), user.getPassword(), user.isEnabled(), authorities);
        {
            sorfaceUser.setId(user.getId());
            sorfaceUser.setFirstName(user.getFirstName());
            sorfaceUser.setLastName(user.getLastName());
            sorfaceUser.setMiddleName(user.getMiddleName());
            sorfaceUser.setAvatarUrl(user.getAvatarUrl());
            sorfaceUser.setEmail(user.getEmail());
            sorfaceUser.setBirthday(user.getBirthday());
        }

        return sorfaceUser;
    }

    private List<GrantedAuthority> convertRoles(final Collection<? extends RoleEntity> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getValue()))
                .collect(Collectors.toList());
    }

}
