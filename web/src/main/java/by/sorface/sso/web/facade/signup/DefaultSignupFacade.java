package by.sorface.sso.web.facade.signup;

import by.sorface.sso.web.dao.models.TokenEntity;
import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.exceptions.NotFoundException;
import by.sorface.sso.web.exceptions.ObjectExpiredException;
import by.sorface.sso.web.exceptions.ObjectInvalidException;
import by.sorface.sso.web.exceptions.UserRequestException;
import by.sorface.sso.web.mappers.UserMapper;
import by.sorface.sso.web.records.requests.AccountSignup;
import by.sorface.sso.web.records.requests.ConfirmEmail;
import by.sorface.sso.web.records.responses.UserConfirm;
import by.sorface.sso.web.records.responses.UserRegisteredHash;
import by.sorface.sso.web.services.tokens.TokenService;
import by.sorface.sso.web.services.users.UserService;
import by.sorface.sso.web.utils.HashUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultSignupFacade implements SignupFacade {

    private final UserService userService;

    private final TokenService tokenService;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserRegisteredHash signup(final AccountSignup user) throws UserRequestException {
        log.info("User registration request received");

        final UserEntity foundUserByEmail = userService.findByEmail(user.email());

        if (Objects.nonNull(foundUserByEmail)) {
            log.warn("A user with [id {}] and [email {}] already exists", foundUserByEmail.getId(), foundUserByEmail.getEmail());
            throw new UserRequestException("The user already exists with this email");
        }

        final UserEntity foundUserByLogin = userService.findByUsername(user.username());

        if (Objects.nonNull(foundUserByLogin)) {
            log.warn("A user with [id {}] and [login {}] already exists", foundUserByLogin.getId(), foundUserByLogin.getEmail());
            throw new UserRequestException("The user already exists with this login");
        }

        log.debug("Preparing the user for saving to the database");

        final UserEntity userEntity = userMapper.map(user);

        final UserEntity savedUser = userService.save(userEntity);

        log.info("A new user has been registered with the ID {}", savedUser.getId());

        log.info("Preparing a hash token for user");

        final TokenEntity registryToken = tokenService.saveForUser(savedUser);

        log.info("The hash token {} created for account", registryToken.getHash().substring(0, 5).concat("..."));

        return new UserRegisteredHash(savedUser.getId(), savedUser.getEmail(), registryToken.getHash());
    }

    @Override
    @Transactional
    public UserConfirm confirm(final ConfirmEmail confirmEmail) {
        log.info("Request for account confirmation using a token {}", HashUtils.shortHash(confirmEmail.token()));

        final var token = tokenService.findByHash(confirmEmail.token());

        if (Objects.isNull(token)) {
            log.warn("The token for confirming the account was not found");

            throw new ObjectInvalidException("The token is invalid");
        }

        log.info("The token received {}", HashUtils.shortHash(token.getHash()));

        final LocalDateTime expiredDate = token.getModifiedDate().plusDays(30);

        if (expiredDate.isBefore(LocalDateTime.now())) {
            log.warn("The token's lifetime has expired. Token expired on  {}. The token belongs to the user with the id {}",
                    expiredDate, token.getUser().getId());

            throw new ObjectExpiredException("The token's lifetime has expired");
        }

        final UserEntity user = token.getUser();

        user.setEnabled(true);

        final UserEntity savedUser = userService.save(user);

        log.info("Account with ID {} confirmed by token hash {}", token.getUser().getId(),
                HashUtils.shortHash(token.getHash()));

        return new UserConfirm(savedUser.getId(), savedUser.getEmail(), savedUser.isEnabled());
    }

    @Override
    @Transactional
    public UserRegisteredHash findTokenByEmail(final String email) {
        final var registryToken = tokenService.findTokenByEmail(email);

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

        return new UserRegisteredHash(user.getId(), user.getEmail(), registryToken.getHash());
    }

}
