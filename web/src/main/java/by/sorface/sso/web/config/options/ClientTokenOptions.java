package by.sorface.sso.web.config.options;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.temporal.ChronoUnit;

@Data
@Configuration
@ConfigurationProperties("sorface.client.token")
public class ClientTokenOptions {

    private TokenSetting accessToken = new TokenSetting();

    private TokenSetting refreshToken = new TokenSetting();

    private TokenSetting authorizationCode = new TokenSetting();

    @Data
    public static class TokenSetting {

        private int timeToLiveValue;

        private ChronoUnit timeToLiveCron;

    }

}
