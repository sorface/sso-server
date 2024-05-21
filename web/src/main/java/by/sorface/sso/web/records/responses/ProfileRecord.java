package by.sorface.sso.web.records.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProfileRecord(UUID id, String email, String firstName, String lastName, String avatar, List<String> roles) {
}
