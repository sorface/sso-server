package by.sorface.sso.web.services.locale;

import by.sorface.sso.web.constants.SupportedLocales;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocaleI18Service implements LocaleService {

    private final ResourceBundleMessageSource messageSource;

    public String getMessage(final String code) {
        return messageSource.getMessage(code, null, SupportedLocales.RU);
    }

    public String getMessage(final String code, final Object... args) {
        return messageSource.getMessage(code, args, SupportedLocales.RU);
    }

}
