package by.sorface.sso.web.facade.signup;

import by.sorface.sso.web.records.mails.Mail;
import by.sorface.sso.web.records.requests.AccountSignup;
import by.sorface.sso.web.records.requests.ResendConfirmEmail;
import by.sorface.sso.web.records.responses.UserRegistered;
import by.sorface.sso.web.services.emails.EmailService;
import by.sorface.sso.web.utils.json.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultSignupEmailFacade implements SignupEmailFacade {

    private final EmailService emailService;

    private final DefaultSignupFacade userRegistryFacade;

    @Override
    public UserRegistered signup(final AccountSignup user) {
        log.info("User registration request received {}{}", System.lineSeparator(), Json.lazyStringifyWithMasking(user));

        final var registry = userRegistryFacade.signup(user);

        log.info("User registration completed. [account id - {}]", registry.id());

        this.sendRegistryEmail(registry.email(), registry.hash());

        return new UserRegistered(registry.id(), registry.email());
    }

    /**
     * @param resendConfirmEmail user's email
     * @return user info
     */
    @Override
    public UserRegistered resendConfirmEmail(final ResendConfirmEmail resendConfirmEmail) {
        final var userRegisteredHash = userRegistryFacade.findTokenByEmail(resendConfirmEmail.email());

        this.sendRegistryEmail(userRegisteredHash.email(), userRegisteredHash.hash());

        return new UserRegistered(userRegisteredHash.id(), userRegisteredHash.email());
    }

    /**
     * Sending an email with a confirmation token
     *
     * @param email user email
     * @param hash  confirm mail
     */
    private void sendRegistryEmail(final String email, final String hash) {
        final var mail = new Mail(email, "Подтверждение регистрации", hash);
        log.info("Preparing an email to confirm the account");
        log.debug("{}{}", System.lineSeparator(), Json.stringify(mail));

        emailService.send(mail);

        log.info("The email to confirm your account has been sent to {}", email);
    }

}
