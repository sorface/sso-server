package by.sorface.ssoserver.facade;

import by.sorface.ssoserver.UserRecord;
import by.sorface.ssoserver.dao.models.RegistryTokenEntity;
import by.sorface.ssoserver.dao.models.UserEntity;
import by.sorface.ssoserver.exceptions.UserRequestException;
import by.sorface.ssoserver.mappers.UserToUserEntityMapper;
import by.sorface.ssoserver.services.RegistryTokenService;
import by.sorface.ssoserver.services.UserService;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistryFacade {

    private final UserService userService;

    private final RegistryTokenService registryTokenService;

    private final UserToUserEntityMapper userToUserEntityMapper;

    @Getter
    @Builder
    public static class UserRegistered {

        private UUID id;

        private String email;

    }

    @Getter
    @Builder
    public static class UserRegisteredHash {

        private UUID id;

        private String email;

        private String hash;

    }

    @Getter
    @Builder
    public static class UserConfirm {

        private UUID id;

        private String email;

        private boolean confirm;

    }

    @Transactional
    public UserRegisteredHash registry(final UserRecord user) throws UserRequestException {
        log.info("Получен запрос на регистрацию пользователя");

        final UserEntity foundUser = userService.findByEmail(user.getEmail());

        if (Objects.nonNull(foundUser)) {
            log.warn("Пользователь с [id {}] и [email {}] уже существует", foundUser.getId(), foundUser.getEmail());
            throw new UserRequestException("Пользователь уже существует с таким email");
        }

        final UserEntity foundUserByLogin = userService.findByUsername(user.getUsername());

        if (Objects.nonNull(foundUserByLogin)) {
            log.warn("Пользователь с [id {}] и [login {}] уже существует", foundUserByLogin.getId(), foundUserByLogin.getEmail());
            throw new UserRequestException("Пользователь уже существует с таким login");
        }

        log.debug("Подготовка пользователя для сохранение в базу данных");

        final UserEntity userEntity = userToUserEntityMapper.map(user);

        final UserEntity savedUser = userService.save(userEntity);

        log.info("Зарегистрирован новый пользователь с ID {}", savedUser.getId());

        final RegistryTokenEntity registryToken = registryTokenService.saveRandomForUser(savedUser);

        log.info("Создан токен {} для подтверждения аккаунта пользователя", registryToken.getHash().substring(0, 5).concat("..."));
        return UserRegisteredHash.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .hash(registryToken.getHash())
                .build();
    }

    @Transactional
    public UserConfirm confirmByToken(final String hash) {
        final var registryToken = registryTokenService.findByHash(hash);

        final UserEntity user = registryToken.getUser();

        user.setEnabled(true);

        final UserEntity savedUser = userService.save(user);

        return UserConfirm.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .confirm(savedUser.isEnabled())
                .build();
    }

}
