package by.sorface.ssoserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing
@EnableGlobalAuthentication
public class SorfaceSsoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SorfaceSsoServerApplication.class, args);
    }

}
