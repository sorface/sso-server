package by.sorface.ssoserver.services;

import by.sorface.ssoserver.dao.models.RegistryTokenEntity;
import by.sorface.ssoserver.dao.models.UserEntity;
import by.sorface.ssoserver.dao.repository.RegistryTokenRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
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

    @Transactional(readOnly = true)
    public RegistryTokenEntity saveRandomForUser(final UserEntity user) {
        final String hash = RandomStringUtils.random(255, true, true);

        final var registryTokenEntity = new RegistryTokenEntity();
        {
            registryTokenEntity.setHash(hash);
            registryTokenEntity.setUser(user);
        }

        return registryTokenRepository.save(registryTokenEntity);
    }

}
