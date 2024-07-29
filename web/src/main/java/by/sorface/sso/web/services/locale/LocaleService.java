package by.sorface.sso.web.services.locale;

import java.util.Map;

public interface LocaleService {

    String getI18Message(final String i18Code);

    String getI18Message(final String i18Code, final Map<String, String> args);

}
