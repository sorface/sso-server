package by.sorface.ssoserver.services;

import by.sorface.ssoserver.dao.models.RegistryTokenEntity;
import by.sorface.ssoserver.dao.models.UserEntity;
import by.sorface.ssoserver.dao.repository.RegistryTokenRepository;
import by.sorface.ssoserver.utils.HashUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistryTokenServiceImpl implements RegistryTokenService {

    private final RegistryTokenRepository registryTokenRepository;

    @Override
    @Transactional(readOnly = true)
    public RegistryTokenEntity findByHash(final String hash) {
        return registryTokenRepository.findByHashIgnoreCase(hash);
    }

    @Transactional
    public RegistryTokenEntity saveRandomForUser(final UserEntity user) {
        final String hash = HashUtils.generateRegistryHash(55);

        final var registryTokenEntity = new RegistryTokenEntity();
        {
            registryTokenEntity.setHash(hash);
            registryTokenEntity.setUser(user);
        }

        return registryTokenRepository.save(registryTokenEntity);
    }

    @Override
    public RegistryTokenEntity findRegistryTokenByUserEmail(final String email) {
        return registryTokenRepository.findByUser_Email(email);
    }

}
