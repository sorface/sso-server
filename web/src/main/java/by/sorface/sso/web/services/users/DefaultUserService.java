package by.sorface.sso.web.services.users;

import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserEntity findByUsername(final String username) {
        return userRepository.findFirstByUsernameIgnoreCase(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findByEmail(final String email) {
        return userRepository.findFirstByEmailIgnoreCase(email);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findByUsernameOrEmail(final String username, final String email) {
        return userRepository.findFirstByUsernameIgnoreCaseOrEmailIgnoreCase(username, email);
    }

    @Override
    @Transactional
    public UserEntity save(final UserEntity user) {
        return userRepository.save(user);
    }

}
