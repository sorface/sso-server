package by.sorface.sso.web.facade.accounts;

import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.dao.models.enums.TokenOperationType;
import by.sorface.sso.web.exceptions.UserRequestException;
import by.sorface.sso.web.facade.emails.EmailLocaleMessageFacade;
import by.sorface.sso.web.records.I18Codes;
import by.sorface.sso.web.records.tokens.ApplyNewPasswordRequest;
import by.sorface.sso.web.services.tokens.TokenService;
import by.sorface.sso.web.services.tokens.TokenValidator;
import by.sorface.sso.web.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DefaultRenewPasswordFacade implements RenewPasswordFacade {

    private final TokenService tokenService;

    private final TokenValidator tokenValidator;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final EmailLocaleMessageFacade emailLocaleMessageFacade;

    @Override
    public void forgetPassword(final String email) {
        final var user = userService.findByEmail(email);

        if (Objects.isNull(user)) {
            throw new UserRequestException(I18Codes.I18UserCodes.NOT_FOUND_BY_EMAIL);
        }

        final var token = tokenService.saveForUser(user, TokenOperationType.PASSWORD_RENEW);

        final Locale locale = LocaleContextHolder.getLocale();

        emailLocaleMessageFacade.sendRenewPasswordEmail(locale, user.getEmail(), token.getHash(), user.getUsername());
    }

    @Override
    @Transactional
    public void applyNewPassword(final ApplyNewPasswordRequest request) {
        final var token = tokenService.findByHash(request.hashToken());

        tokenValidator.validateOperation(token, TokenOperationType.PASSWORD_RENEW);
        tokenValidator.validateExpiredDate(token);

        final UserEntity user = token.getUser();

        final String encodedPassword = passwordEncoder.encode(request.newPassword());

        user.setPassword(encodedPassword);

        userService.save(user);

        tokenService.deleteByHash(token.getHash());
    }

    @Override
    public void checkRenewPasswordToken(final String hash) {
        final var token = tokenService.findByHash(hash);

        tokenValidator.validateOperation(token, TokenOperationType.PASSWORD_RENEW);
        tokenValidator.validateExpiredDate(token);
    }

}
