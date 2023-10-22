package by.sorface.ssoserver.services.providers;

import by.sorface.ssoserver.exceptions.UserConfirmException;
import by.sorface.ssoserver.mappers.SorfaceUserMapper;
import by.sorface.ssoserver.records.SorfaceUser;
import by.sorface.ssoserver.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SorfaceUserDatabaseProvider implements UserDetailsService {

    private final UserService userRepository;

    private final SorfaceUserMapper sorfaceUserMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final var sorfaceUser = Optional.ofNullable(userRepository.findByUsernameOrEmail(username, username))
                .map(sorfaceUserMapper::to)
                .orElseThrow(() -> new UsernameNotFoundException("User with username {%s} not found".formatted(username)));

        if (!sorfaceUser.isEnabled()) {
            throw new UserConfirmException("User is not confirm");
        }

        return sorfaceUser;
    }
}
