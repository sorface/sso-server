package by.sorface.sso.web.controllers.advices;

import by.sorface.sso.web.controllers.AccountController;
import by.sorface.sso.web.controllers.AccountSessionController;
import by.sorface.sso.web.controllers.ApplicationClientController;
import by.sorface.sso.web.exceptions.NotFoundException;
import by.sorface.sso.web.exceptions.ObjectExpiredException;
import by.sorface.sso.web.exceptions.UnauthorizedException;
import by.sorface.sso.web.exceptions.UserRequestException;
import by.sorface.sso.web.records.responses.OperationError;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(
        basePackageClasses = {
                AccountController.class,
                AccountSessionController.class,
                ApplicationClientController.class
        }
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

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public OperationError handleUnauthorizedException(final UnauthorizedException e) {
        return buildError(HttpStatus.UNAUTHORIZED, e);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public OperationError handleException(final ValidationException e) {
        return buildError(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public OperationError handleNotFoundException(final RuntimeException e) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public OperationError handleException(final Exception e) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private OperationError buildError(final HttpStatus status, final Exception exception) {
        return new OperationError(exception.getMessage(), status.getReasonPhrase(), status.value());
    }

}
