package by.sorface.ssoserver.records.responses;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserConfirm {

    private UUID id;

    private String email;

    private boolean confirm;

}
