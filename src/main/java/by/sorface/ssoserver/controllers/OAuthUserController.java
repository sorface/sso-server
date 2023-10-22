package by.sorface.ssoserver.controllers;

import by.sorface.ssoserver.UserRecord;
import by.sorface.ssoserver.facade.UserFacadeService;
import by.sorface.ssoserver.records.responses.UserConfirm;
import by.sorface.ssoserver.records.responses.UserRegistered;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class OAuthUserController {

    private final UserFacadeService userFacadeService;

    @PostMapping("/registry")
    public ResponseEntity<UserRegistered> registry(@RequestBody final UserRecord user) {
        final UserRegistered userRegistered = userFacadeService.executeProcessRegistry(user);

        return ResponseEntity.ok(userRegistered);
    }

    @PostMapping("/confirm")
    public ResponseEntity<UserConfirm> registry(@RequestParam("token") final String token) {
        final UserConfirm userRegistered = userFacadeService.executeProcessConfirmation(token);

        return ResponseEntity.ok(userRegistered);
    }

}
