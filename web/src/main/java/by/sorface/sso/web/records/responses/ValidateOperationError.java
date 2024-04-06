package by.sorface.sso.web.records.responses;

import lombok.Data;

import java.util.List;

public record ValidateOperationError(String message, List<ValidateError> errors, int code) {

    @Data
    public static class ValidateError {

        private String field;

        private String message;

    }

}
