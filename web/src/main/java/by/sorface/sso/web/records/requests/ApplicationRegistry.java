package by.sorface.sso.web.records.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record ApplicationRegistry(
        @NotBlank(message = "client.registry.name.empty-or-null")
        String name,

        @NotEmpty(message = "client.registry.redirect-url.empty")
        Set<String> redirectionUrls
) {
}
