package by.sorface.sso.web.records.responses;

import java.util.UUID;

public record ProfileRecord(UUID id, String email, String firstName, String lastName) {
}
