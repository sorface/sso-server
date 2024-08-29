package by.sorface.sso.web.facade.accounts;

import by.sorface.sso.web.config.options.OAuth2Options;
import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.dao.models.enums.TokenOperationType;
import by.sorface.sso.web.exceptions.UserRequestException;
import by.sorface.sso.web.records.I18Codes;
import by.sorface.sso.web.records.mails.MailImage;
import by.sorface.sso.web.records.mails.MailTemplate;
import by.sorface.sso.web.records.tokens.ApplyNewPasswordRequest;
import by.sorface.sso.web.services.emails.EmailService;
import by.sorface.sso.web.services.locale.LocaleI18Service;
import by.sorface.sso.web.services.tokens.TokenService;
import by.sorface.sso.web.services.tokens.TokenValidator;
import by.sorface.sso.web.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DefaultRenewPasswordFacade implements RenewPasswordFacade {

    private final TokenService tokenService;

    private final TokenValidator tokenValidator;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final OAuth2Options oAuth2Options;

    private final LocaleI18Service localeI18Service;

    @Override
    public void forgetPassword(final String email) {
        final var user = userService.findByEmail(email);

        if (Objects.isNull(user)) {
            throw new UserRequestException(I18Codes.I18UserCodes.NOT_FOUND_BY_EMAIL);
        }

        final var token = tokenService.saveForUser(user, TokenOperationType.PASSWORD_RENEW);

        CompletableFuture.runAsync(() -> {
            final var context = new Context();
            {
                context.setVariable("token", token.getHash());
                context.setVariable("issuer", oAuth2Options.getIssuerUrl());
            }

            final var emailTemplate = localeI18Service.getI18Message(I18Codes.I18EmailCodes.TEMPLATE);
            final var subject = localeI18Service.getI18Message(I18Codes.I18EmailCodes.CONFIRMATION_REGISTRATION);

            final var images = List.of(new MailImage("isorface.png"));

            final var mailTemplate = new MailTemplate(user.getEmail(), subject, emailTemplate, context, images);

            emailService.sendHtml(mailTemplate);
        });
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
    }

    @Override
    public void checkRenewPasswordToken(final String hash) {
        final var token = tokenService.findByHash(hash);

        tokenValidator.validateOperation(token, TokenOperationType.PASSWORD_RENEW);
        tokenValidator.validateExpiredDate(token);
    }

}
