package by.sorface.sso.web.exceptions;

import java.util.HashMap;
import java.util.Map;

public class UserRequestException extends RuntimeException {

    private final Map<String, String> args = new HashMap<>();

    public UserRequestException(final String message) {
        super(message);
    }

    public UserRequestException(final String message, final Map<String, String> args) {
        super(message);

        args.putAll(args);
    }

    public Map<String, String> getArgs() {
        return args;
    }

}
