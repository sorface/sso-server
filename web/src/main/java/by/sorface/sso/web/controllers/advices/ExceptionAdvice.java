package by.sorface.sso.web.controllers.advices;

import by.sorface.sso.web.controllers.OAuthUserController;
import by.sorface.sso.web.exceptions.NotFoundException;
import by.sorface.sso.web.exceptions.ObjectExpiredException;
import by.sorface.sso.web.exceptions.UserRequestException;
import by.sorface.sso.web.records.responses.OperationError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(
        basePackageClasses = OAuthUserController.class
)
public class ExceptionAdvice {

    @ExceptionHandler(UserRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public OperationError handleUserRequestException(final UserRequestException e) {
        return buildError(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public OperationError handleNotFoundException(final NotFoundException e) {
        return buildError(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(ObjectExpiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public OperationError handleObjectExpiredException(final ObjectExpiredException e) {
        return buildError(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public OperationError handleNotFoundException(final RuntimeException e) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private OperationError buildError(final HttpStatus status, final Exception e) {
        return OperationError.builder()
                .code(status.value())
                .details(e.getMessage())
                .message(status.getReasonPhrase())
                .build();
    }

}
