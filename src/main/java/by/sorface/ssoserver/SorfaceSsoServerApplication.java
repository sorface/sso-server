package by.sorface.ssoserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.env.EnvironmentEndpoint;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing
public class SorfaceSsoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SorfaceSsoServerApplication.class, args);
    }

    @Autowired
    private EnvironmentEndpoint environmentEndpoint;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        String valueAsString = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(environmentEndpoint.environment(""));

        log.debug(valueAsString);
    }

}
