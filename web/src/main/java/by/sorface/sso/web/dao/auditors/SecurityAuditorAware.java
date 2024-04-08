package by.sorface.sso.web.dao.auditors;

import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.records.principals.DefaultPrincipal;
import by.sorface.sso.web.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityAuditorAware implements AuditorAware<UserEntity> {

    private final UserService userService;

    @SuppressWarnings("NullableProblems")
    @Override
    public Optional<UserEntity> getCurrentAuditor() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication) || !authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            return Optional.empty();
        }

        final var principal = (DefaultPrincipal) authentication.getPrincipal();

        return Optional.ofNullable(userService.findById(principal.getId()));
    }

}
