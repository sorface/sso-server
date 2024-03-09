package by.sorface.sso.web.facade.signup;

import by.sorface.sso.web.records.MailRequest;
import by.sorface.sso.web.records.requests.AccountSignup;
import by.sorface.sso.web.records.requests.ResendConfirmEmail;
import by.sorface.sso.web.records.responses.UserRegistered;
import by.sorface.sso.web.services.emails.EmailService;
import by.sorface.sso.web.utils.json.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

        final var mails = List.of(
                new MailRequest(user.email(), "Подтверждение регистрации", registry.hash())
        );

        log.info("Preparing an email to confirm the account");
        log.debug("{}{}", System.lineSeparator(), Json.stringify(mails));

        emailService.send(mails);

        log.info("The email to confirm your account has been sent to {}", registry.email());

        return new UserRegistered(registry.id(), registry.email());
    }

    @Override
    public UserRegistered resendConfirmationEmail(final ResendConfirmEmail resendConfirmEmail) {
        final var userRegisteredHash = userRegistryFacade.findTokenByEmail(resendConfirmEmail.email());

        final var mails = List.of(
                new MailRequest(
                        userRegisteredHash.email(),
                        "Подтверждение регистрации",
                        userRegisteredHash.hash()
                )
        );

        emailService.send(mails);

        return new UserRegistered(userRegisteredHash.id(), userRegisteredHash.email());
    }

}
