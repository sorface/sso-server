package by.sorface.sso.web.records.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ApplicationClientDelete(
        @NotNull(message = "validate.client.delete.id.null")
        @NotEmpty(message = "validate.client.delete.id.empty")
        @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "validate.client.delete.id.regexp")
        String id) {
}
