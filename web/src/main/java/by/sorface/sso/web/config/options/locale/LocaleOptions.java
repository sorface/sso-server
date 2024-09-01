package by.sorface.sso.web.config.options.locale;

import by.sorface.sso.web.constants.SupportedLocales;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Data
@Component
@ConfigurationProperties("sorface.locale")
public class LocaleOptions {

    private Locale defaultLocale = SupportedLocales.RU;

    private String changeLocaleParameterName = "lang";

    private HttpMethod[] changeLocaleMethods = new HttpMethod[]{
            HttpMethod.POST
    };

    private LocaleCookieOptions cookie = new LocaleCookieOptions();

}
