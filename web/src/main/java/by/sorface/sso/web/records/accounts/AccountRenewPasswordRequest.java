package by.sorface.sso.web.records.accounts;

import by.sorface.sso.web.records.I18Codes;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AccountRenewPasswordRequest(
        @NotNull(message = I18Codes.I18UserCodes.EMAIL_NOT_VALID)
        @NotEmpty(message = I18Codes.I18UserCodes.EMAIL_NOT_VALID)
        @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$", message = I18Codes.I18UserCodes.EMAIL_NOT_VALID)
        String email) {
}
