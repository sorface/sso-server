package by.sorface.sso.web.facade.signup;

import by.sorface.sso.web.config.properties.SorfaceSsoProperties;
import by.sorface.sso.web.records.mails.MailImage;
import by.sorface.sso.web.records.mails.MailTemplate;
import by.sorface.sso.web.records.requests.AccountSignup;
import by.sorface.sso.web.records.requests.ResendConfirmEmail;
import by.sorface.sso.web.records.responses.UserRegistered;
import by.sorface.sso.web.records.responses.UserRegisteredHash;
import by.sorface.sso.web.services.emails.EmailService;
import by.sorface.sso.web.services.locale.LocaleI18Service;
import by.sorface.sso.web.utils.json.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultSignupEmailFacade implements SignupEmailFacade {

    private final EmailService emailService;

    private final DefaultSignupFacade userRegistryFacade;

    private final SorfaceSsoProperties sorfaceSsoProperties;

    private final LocaleI18Service localeI18Service;

    @Override
    public UserRegistered signup(final AccountSignup user) {
        log.info("User registration request received {}{}", System.lineSeparator(), Json.lazyStringifyWithMasking(user));

        final var registry = userRegistryFacade.signup(user);

        log.info("User registration completed. [account id - {}]", registry.id());

        this.sendRegistryEmail(registry);

        return new UserRegistered(registry.id(), registry.email());
    }

    /**
     * @param resendConfirmEmail user's email
     * @return user info
     */
    @Override
    public UserRegistered resendConfirmEmail(final ResendConfirmEmail resendConfirmEmail) {
        final var registry = userRegistryFacade.findTokenByEmail(resendConfirmEmail.email());

        this.sendRegistryEmail(registry);

        return new UserRegistered(registry.id(), registry.email());
    }

    /**
     * Sending an email with a confirmation token
     *
     * @param registeredUser user information
     */
    private void sendRegistryEmail(final UserRegisteredHash registeredUser) {
        final var context = new Context();
        {
            context.setVariable("token", registeredUser.hash());
            context.setVariable("issuer", sorfaceSsoProperties.getIssuerUrl());
        }

        final var emailTemplate = localeI18Service.getMessage("email.confirm");
        final var subject = localeI18Service.getMessage("email.confirm.subject");
        final var images = List.of(new MailImage("isorface.png"));

        final var mailTemplate = new MailTemplate(registeredUser.email(), subject, emailTemplate, context, images);

        log.info("Preparing an email to confirm the account");
        log.debug("{}{}", System.lineSeparator(), Json.stringify(mailTemplate));

        emailService.sendHtml(mailTemplate);

        log.info("The email to confirm your account has been sent to {}", registeredUser.email());
    }

}
