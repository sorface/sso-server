package by.sorface.sso.web.records.responses;

import java.util.UUID;

public record UserRegisteredHash(UUID id, String email, String hash) {
}
