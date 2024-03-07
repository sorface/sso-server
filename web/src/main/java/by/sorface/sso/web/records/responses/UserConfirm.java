package by.sorface.sso.web.records.responses;

import java.util.UUID;

public record UserConfirm(UUID id, String email, boolean confirm) {
}
