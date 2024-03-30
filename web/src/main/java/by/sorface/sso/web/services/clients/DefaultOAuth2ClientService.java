package by.sorface.sso.web.services.clients;

import by.sorface.sso.web.dao.models.OAuth2Client;
import by.sorface.sso.web.dao.repository.OAuth2ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultOAuth2ClientService implements OAuth2ClientService {

    private final OAuth2ClientRepository oAuth2ClientRepository;

    @Transactional
    public OAuth2Client save(final OAuth2Client oAuth2Client) {
        return oAuth2ClientRepository.save(oAuth2Client);
    }

    @Transactional(readOnly = true)
    public OAuth2Client findByClientId(final String clientId) {
        return oAuth2ClientRepository.findByClientId(clientId);
    }

    @Override
    public OAuth2Client findById(final UUID id) {
        return oAuth2ClientRepository.findById(id).orElse(null);
    }

    @Override
    public List<OAuth2Client> findAllByUserId(final UUID id) {
        return oAuth2ClientRepository.findAllByCreatedBy_Id(id);
    }

    @Override
    public OAuth2Client findByIdAndUserId(final UUID id, final UUID userId) {
        return oAuth2ClientRepository.findByIdAndCreatedBy_Id(id, userId);
    }

}
