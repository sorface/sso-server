package by.sorface.sso.web.mappers;

import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.records.UserRegistryRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity map(UserRegistryRecord user) {
        final var userEntity = new UserEntity();

        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setMiddleName(user.getMiddleName());
        userEntity.setEnabled(false);

        final var passwordHash = passwordEncoder.encode(user.getPassword());
        userEntity.setPassword(passwordHash);

        return userEntity;
    }

}
