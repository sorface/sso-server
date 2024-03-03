package by.sorface.ssoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;

@SpringBootApplication
@EnableJpaAuditing
@EnableGlobalAuthentication
public class SorfaceSsoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SorfaceSsoServerApplication.class, args);
    }

}
