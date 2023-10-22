package by.sorface.ssoserver.controllers;

import by.sorface.ssoserver.UserRecord;
import by.sorface.ssoserver.facade.UserFacadeService;
import by.sorface.ssoserver.facade.UserRegistryFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class OAuthUserController {

    private final UserFacadeService userFacadeService;

    @PostMapping("/registry")
    public ResponseEntity<UserRegistryFacade.UserRegistered> registry(@RequestBody final UserRecord user) {
        final UserRegistryFacade.UserRegistered userRegistered = userFacadeService.executeProcessRegistry(user);

        return ResponseEntity.ok(userRegistered);
    }

    @PostMapping("/registry/confirm")
    public ResponseEntity<UserRegistryFacade.UserConfirm> registry(@RequestParam("token") final String token) {
        final UserRegistryFacade.UserConfirm userRegistered = userFacadeService.executeProcessConfirmation(token);

        return ResponseEntity.ok(userRegistered);
    }

}
