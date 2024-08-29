package by.sorface.sso.web.records.tokens;

public record ApplyNewPasswordRequest(
        String newPassword,
        String hashToken
) {
}
