package by.sorface.ssoserver.facade;

import by.sorface.ssoserver.records.UserRecord;
import by.sorface.ssoserver.records.MailRequest;
import by.sorface.ssoserver.records.responses.UserConfirm;
import by.sorface.ssoserver.records.responses.UserRegistered;
import by.sorface.ssoserver.records.responses.UserRegisteredHash;
import by.sorface.ssoserver.services.EmailService;
import by.sorface.ssoserver.utils.json.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultUserFacadeService implements UserFacadeService {

    private final EmailService emailService;

    private final UserRegistryFacade userRegistryFacade;

    @Override
    public UserRegistered registry(final UserRecord user) {
        final UserRegisteredHash registry = userRegistryFacade.registry(user);

        log.info("User registration completed. [account id - {}]", registry.getId());

        final var mails = List.of(
                new MailRequest(user.getEmail(), "Подтверждение регистрации", registry.getHash())
        );

        log.info("Preparing an email to confirm the account");
        log.debug("{}{}", System.lineSeparator(), Json.stringify(mails));

        emailService.send(mails);

        log.info("The email to confirm your account has been sent to {}", registry.getEmail());

        return UserRegistered.builder()
                .id(registry.getId())
                .email(registry.getEmail())
                .build();
    }

    @Override
    public UserConfirm confirmEmail(final String token) {
        return userRegistryFacade.confirmByToken(token);
    }

    public UserRegistered resendConfirmEmail(final String email) {
        final UserRegisteredHash userRegisteredHash = userRegistryFacade.findRegisteredTokenByEmail(email);

        final var mails = List.of(
                new MailRequest(userRegisteredHash.getEmail(), "Подтверждение регистрации", userRegisteredHash.getHash())
        );

        emailService.send(mails);

        return UserRegistered.builder()
                .id(userRegisteredHash.getId())
                .email(userRegisteredHash.getEmail())
                .build();
    }

}
