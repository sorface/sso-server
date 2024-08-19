package by.sorface.sso.web.dao.repository;

import by.sorface.sso.web.dao.models.RoleEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<RoleEntity> {

    Optional<RoleEntity> findByValueIgnoreCase(String value);

}
