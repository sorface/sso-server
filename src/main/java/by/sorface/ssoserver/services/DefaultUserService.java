package by.sorface.ssoserver.services;

import by.sorface.ssoserver.dao.models.UserEntity;
import by.sorface.ssoserver.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserEntity findByUsername(String username) {
        return userRepository.findFirstByUsernameIgnoreCase(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findByEmail(String email) {
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
