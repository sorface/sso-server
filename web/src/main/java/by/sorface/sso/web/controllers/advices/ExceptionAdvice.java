package by.sorface.sso.web.controllers.advices;

import by.sorface.sso.web.controllers.AccountController;
import by.sorface.sso.web.controllers.AccountSessionController;
import by.sorface.sso.web.controllers.ApplicationClientController;
import by.sorface.sso.web.exceptions.NotFoundException;
import by.sorface.sso.web.exceptions.ObjectExpiredException;
import by.sorface.sso.web.exceptions.UnauthorizedException;
import by.sorface.sso.web.exceptions.UserRequestException;
import by.sorface.sso.web.records.responses.OperationError;
import by.sorface.sso.web.records.responses.ValidateOperationError;
import by.sorface.sso.web.services.locale.LocaleI18Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice(
        basePackageClasses = {
                AccountController.class,
                AccountSessionController.class,
                ApplicationClientController.class
        }
)
@RequiredArgsConstructor
public class ExceptionAdvice {

    private final LocaleI18Service localeI18Service;

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
        return buildError(HttpStatus.UNAUTHORIZED, "exception.access.denied");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidateOperationError handleException(final MethodArgumentNotValidException e) {
        final List<ValidateOperationError.ValidateError> errors = e.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    final var validateError = new ValidateOperationError.ValidateError();
                    {
                        validateError.setField(((FieldError) error).getField());
                        validateError.setMessage(localeI18Service.getMessage(error.getDefaultMessage()));
                    }

                    return validateError;
                })
                .toList();

        return buildValidateError(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public OperationError handleAccessDenied(final AccessDeniedException e) {
        return buildError(HttpStatus.UNAUTHORIZED, "exception.access.denied");
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public OperationError handleInsufficientAuthentication(final InsufficientAuthenticationException e) {
        return buildError(HttpStatus.UNAUTHORIZED, "exception.access.denied");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public OperationError handleAuthenticationException(final AuthenticationException e) {
        return buildError(HttpStatus.FORBIDDEN, "exception.access.denied");
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
        return new OperationError(localeI18Service.getMessage(exception.getMessage()), status.getReasonPhrase(), status.value());
    }

    private OperationError buildError(final HttpStatus status, final String message) {
        return new OperationError(localeI18Service.getMessage(message), status.getReasonPhrase(), status.value());
    }

    private ValidateOperationError buildValidateError(final HttpStatus status, final List<ValidateOperationError.ValidateError> errors) {
        return new ValidateOperationError(localeI18Service.getMessage("validate.error"), errors, status.value());
    }

}
