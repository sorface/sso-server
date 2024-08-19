package by.sorface.sso.web.services.users;

import by.sorface.sso.web.dao.models.RoleEntity;
import by.sorface.sso.web.dao.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultRoleService implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleEntity findByValue(String name) {
        return roleRepository.findByValueIgnoreCase(name).orElse(null);
    }

}
