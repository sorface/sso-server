package by.sorface.sso.web.exceptions;

import org.springframework.http.HttpStatus;

public class ObjectExpiredException extends GlobalSystemException {

    public ObjectExpiredException(final String i18Codes) {
        super(i18Codes, HttpStatus.BAD_REQUEST);
    }

}
