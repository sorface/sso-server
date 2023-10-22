package by.sorface.ssoserver.controllers.advices;

import by.sorface.ssoserver.controllers.OAuthUserController;
import by.sorface.ssoserver.exceptions.NotFoundException;
import by.sorface.ssoserver.exceptions.UserRequestException;
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
    public ErrorDTO handleUserRequestException(final UserRequestException e) {
        return createErrorDTO(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFoundException(final NotFoundException e) {
        return createErrorDTO(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleNotFoundException(final RuntimeException e) {
        return createErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private ErrorDTO createErrorDTO(final HttpStatus status, final Exception e) {
        return ErrorDTO.builder()
                .code(status.value())
                .details(e.getMessage())
                .message(status.getReasonPhrase())
                .build();
    }

}
