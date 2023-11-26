package by.sorface.ssoserver.services.providers;

import by.sorface.ssoserver.mappers.SorfaceUserMapper;
import by.sorface.ssoserver.services.UserService;
import lombok.RequiredArgsConstructor;
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

    /**
     * Getting an authorized user from the database
     *
     * @param username the username identifying the user whose data is required.
     * @return user details
     * @throws UsernameNotFoundException when user not found by login or email
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final var user = userRepository.findByUsernameOrEmail(username, username);

        return Optional.ofNullable(user)
                .map(sorfaceUserMapper::to)
                .orElseThrow(() -> new UsernameNotFoundException("User with username or email {%s} not found".formatted(username)));
    }

}
