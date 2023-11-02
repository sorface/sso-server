package by.sorface.ssoserver.services.providers;

import by.sorface.ssoserver.dao.models.UserEntity;
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

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final UserEntity entity = userRepository.findByUsername(username);

        return Optional.ofNullable(entity)
                .map(sorfaceUserMapper::to)
                .orElseThrow(() -> new UsernameNotFoundException("User with username {%s} not found".formatted(username)));
    }
}
