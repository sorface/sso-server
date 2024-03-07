package by.sorface.sso.web.controllers;

import by.sorface.sso.web.facade.UserFacadeService;
import by.sorface.sso.web.records.SfPrincipal;
import by.sorface.sso.web.records.UserRegistryRecord;
import by.sorface.sso.web.records.responses.UserConfirm;
import by.sorface.sso.web.records.responses.UserRegistered;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @PostMapping("/signup")
    public ResponseEntity<Object> registryWithSignIn(@RequestBody final UserRegistryRecord userRegistry, HttpServletRequest request) {
        userFacadeService.registry(userRegistry);

        try {
            request.login(userRegistry.email(), userRegistry.password());
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

        final var auth = (Authentication) request.getUserPrincipal();
        final var user = (SfPrincipal) auth.getPrincipal();

        return ResponseEntity.ok(user.getEmail());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/confirm")
    public ResponseEntity<UserConfirm> confirm(@RequestParam("token") final String token) {
        final UserConfirm userRegistered = userFacadeService.confirmEmail(token);

        return ResponseEntity.ok(userRegistered);
    }

    @PostMapping("/confirm/{email}/resend")
    public ResponseEntity<UserRegistered> resendConfirmEmail(@PathVariable(name = "email") final String email) {
        final UserRegistered userRegistered = userFacadeService.resendConfirmationEmail(email);

        return ResponseEntity.ok(userRegistered);
    }

}
