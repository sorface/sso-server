package by.sorface.ssoserver.controllers;

import by.sorface.ssoserver.records.UserRegistryRecord;
import by.sorface.ssoserver.facade.UserFacadeService;
import by.sorface.ssoserver.records.responses.UserConfirm;
import by.sorface.ssoserver.records.responses.UserRegistered;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class OAuthUserController {

    private final UserFacadeService userFacadeService;

    @PostMapping("/registry")
    public ResponseEntity<UserRegistered> registry(@RequestBody final UserRegistryRecord user) {
        final UserRegistered userRegistered = userFacadeService.registry(user);

        return ResponseEntity.ok(userRegistered);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/confirm")
    public ResponseEntity<UserConfirm> registry(@RequestParam("token") final String token) {
        final UserConfirm userRegistered = userFacadeService.confirmEmail(token);

        return ResponseEntity.ok(userRegistered);
    }

    @PostMapping("/confirm/{email}/resend")
    public ResponseEntity<UserRegistered> resendConfirmEmail(@PathVariable(name = "email") final String email) {
        final UserRegistered userRegistered = userFacadeService.resendConfirmationEmail(email);

        return ResponseEntity.ok(userRegistered);
    }

}
