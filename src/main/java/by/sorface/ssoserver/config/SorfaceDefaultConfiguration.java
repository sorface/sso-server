package by.sorface.ssoserver.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {
        SorfaceTokenProperties.class
})
public class SorfaceDefaultConfiguration {
}
