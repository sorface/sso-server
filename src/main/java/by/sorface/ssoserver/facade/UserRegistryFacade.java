package by.sorface.ssoserver.facade;

import by.sorface.ssoserver.records.UserRecord;
import by.sorface.ssoserver.dao.models.RegistryTokenEntity;
import by.sorface.ssoserver.dao.models.UserEntity;
import by.sorface.ssoserver.exceptions.NotFoundException;
import by.sorface.ssoserver.exceptions.ObjectExpiredException;
import by.sorface.ssoserver.exceptions.ObjectInvalidException;
import by.sorface.ssoserver.exceptions.UserRequestException;
import by.sorface.ssoserver.mappers.UserToUserEntityMapper;
import by.sorface.ssoserver.records.responses.UserConfirm;
import by.sorface.ssoserver.records.responses.UserRegisteredHash;
import by.sorface.ssoserver.services.RegistryTokenService;
import by.sorface.ssoserver.services.UserService;
import by.sorface.ssoserver.utils.HashUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistryFacade {

    private final UserService userService;

    private final RegistryTokenService registryTokenService;

    private final UserToUserEntityMapper userToUserEntityMapper;

    @Transactional
    public UserRegisteredHash registry(final UserRecord user) throws UserRequestException {
        log.info("User registration request received");

        final UserEntity foundUserByEmail = userService.findByEmail(user.getEmail());

        if (Objects.nonNull(foundUserByEmail)) {
            log.warn("A user with [id {}] and [email {}] already exists", foundUserByEmail.getId(), foundUserByEmail.getEmail());
            throw new UserRequestException("The user already exists with this email");
        }

        final UserEntity foundUserByLogin = userService.findByUsername(user.getUsername());

        if (Objects.nonNull(foundUserByLogin)) {
            log.warn("A user with [id {}] and [login {}] already exists", foundUserByLogin.getId(), foundUserByLogin.getEmail());
            throw new UserRequestException("The user already exists with this login");
        }

        log.debug("Preparing the user for saving to the database");

        final UserEntity userEntity = userToUserEntityMapper.map(user);

        final UserEntity savedUser = userService.save(userEntity);

        log.info("A new user has been registered with the ID {}", savedUser.getId());

        log.info("Preparing a hash token for user");

        final RegistryTokenEntity registryToken = registryTokenService.saveRandomForUser(savedUser);

        log.info("The hash token {} created for account", registryToken.getHash().substring(0, 5).concat("..."));

        return UserRegisteredHash.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .hash(registryToken.getHash())
                .build();
    }

    @Transactional
    public UserConfirm confirmByToken(final String hash) {
        log.info("Request for account confirmation using a token {}", HashUtils.shortHash(hash));

        final var registryToken = registryTokenService.findByHash(hash);

        if (Objects.isNull(registryToken)) {
            log.warn("The token for confirming the account was not found");

            throw new ObjectInvalidException("The token is invalid");
        }

        log.info("The token received {}", HashUtils.shortHash(registryToken.getHash()));

        final LocalDateTime expiredDate = registryToken.getModifiedDate().plusDays(30);

        if (expiredDate.isBefore(LocalDateTime.now())) {
            log.warn("The token's lifetime has expired. Token expired on  {}. The token belongs to the user with the id {}",
                    expiredDate, registryToken.getUser().getId());

            throw new ObjectExpiredException("The token's lifetime has expired");
        }

        final UserEntity user = registryToken.getUser();

        user.setEnabled(true);

        final UserEntity savedUser = userService.save(user);

        log.info("Account with ID {} confirmed by token hash {}", registryToken.getUser().getId(),
                HashUtils.shortHash(registryToken.getHash()));

        return UserConfirm.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .confirm(savedUser.isEnabled())
                .build();
    }

    @Transactional
    public UserRegisteredHash findRegisteredTokenByEmail(final String email) {
        final var registryToken = registryTokenService.findRegistryTokenByUserEmail(email);

        if (Objects.isNull(registryToken)) {
            log.warn("User not found by email {}", email);

            throw new NotFoundException("User not found by email");
        }

        log.info("The token received {}", HashUtils.shortHash(registryToken.getHash()));

        final LocalDateTime expiredDate = registryToken.getModifiedDate().plusDays(30);

        if (expiredDate.isBefore(LocalDateTime.now())) {
            log.warn("The token's lifetime has expired. Token expired on  {}. The token belongs to the user with the id {}",
                    expiredDate, registryToken.getUser().getId());

            throw new ObjectExpiredException("The token's lifetime has expired");
        }

        final UserEntity user = registryToken.getUser();

        return UserRegisteredHash.builder()
                .id(user.getId())
                .email(user.getEmail())
                .hash(registryToken.getHash())
                .build();
    }

}
