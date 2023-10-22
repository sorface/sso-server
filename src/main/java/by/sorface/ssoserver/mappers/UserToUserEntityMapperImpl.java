package by.sorface.ssoserver.mappers;

import by.sorface.ssoserver.UserRecord;
import by.sorface.ssoserver.dao.models.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserToUserEntityMapperImpl implements UserToUserEntityMapper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity map(UserRecord user) {
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
