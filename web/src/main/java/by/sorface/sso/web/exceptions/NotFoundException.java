package by.sorface.sso.web.exceptions;

import java.util.Map;

public class NotFoundException extends UserRequestException {

    public NotFoundException(final String message) {
        super(message);
    }

    public NotFoundException(String message, Map<String, String> args) {
        super(message, args);
    }

}
