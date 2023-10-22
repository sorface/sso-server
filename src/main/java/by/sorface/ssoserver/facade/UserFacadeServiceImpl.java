package by.sorface.ssoserver.facade;

import by.sorface.ssoserver.UserRecord;
import by.sorface.ssoserver.services.EmailService;
import by.sorface.ssoserver.services.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacadeServiceImpl implements UserFacadeService {

    private final EmailService emailService;

    private final UserRegistryFacade userRegistryFacade;

    @Override
    public UserRegistryFacade.UserRegistered executeProcessRegistry(final UserRecord user) {
        final UserRegistryFacade.UserRegisteredHash registry = userRegistryFacade.registry(user);

        final var mails = List.of(
                new EmailServiceImpl.MailRequest(user.getEmail(), "Подтверждение регистрации", registry.getHash())
        );

        emailService.send(mails);

        return UserRegistryFacade.UserRegistered.builder()
                .id(registry.getId())
                .email(registry.getEmail())
                .build();
    }

    @Override
    public UserRegistryFacade.UserConfirm executeProcessConfirmation(final String token) {
        return userRegistryFacade.confirmByToken(token);
    }

}
