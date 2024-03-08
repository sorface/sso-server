package by.sorface.sso.web.services.tokens;

import by.sorface.sso.web.dao.models.TokenEntity;
import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.dao.repository.RegistryTokenRepository;
import by.sorface.sso.web.utils.HashUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultTokenService implements TokenService {

    private final RegistryTokenRepository registryTokenRepository;

    @Override
    @Transactional(readOnly = true)
    public TokenEntity findByHash(final String hash) {
        return registryTokenRepository.findByHashIgnoreCase(hash);
    }

    @Transactional
    public TokenEntity saveForUser(final UserEntity user) {
        final String hash = HashUtils.generateRegistryHash(10);

        final var registryTokenEntity = new TokenEntity();
        {
            registryTokenEntity.setHash(hash);
            registryTokenEntity.setUser(user);
        }

        return registryTokenRepository.save(registryTokenEntity);
    }

    @Override
    public TokenEntity findTokenByEmail(final String email) {
        return registryTokenRepository.findByUser_Email(email);
    }

}
