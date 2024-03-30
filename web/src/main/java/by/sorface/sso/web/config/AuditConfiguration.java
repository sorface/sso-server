package by.sorface.sso.web.config;

import by.sorface.sso.web.dao.auditors.SecurityAuditorAware;
import by.sorface.sso.web.dao.models.UserEntity;
import by.sorface.sso.web.services.users.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfiguration {

    @Bean
    public AuditorAware<UserEntity> auditorProvider(final UserService userService) {
        return new SecurityAuditorAware(userService);
    }

}
