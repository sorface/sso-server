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
        return userRepository.findFirstByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findByEmail(String username) {
        return userRepository.findFirstByEmail(username);
    }

    @Override
    @Transactional
    public UserEntity save(final UserEntity user) {
        return userRepository.save(user);
    }

}
