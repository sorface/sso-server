package by.sorface.sso.web.services.locale;

public interface LocaleService {

    String getMessage(final String code);

    String getMessage(final String code, final Object... args);

}
