package by.sorface.ssoserver.controllers;

import by.sorface.ssoserver.records.UserRegistryRecord;
import by.sorface.ssoserver.facade.UserFacadeService;
import by.sorface.ssoserver.records.responses.UserConfirm;
import by.sorface.ssoserver.records.responses.UserRegistered;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class OAuthUserController {

    private final UserFacadeService userFacadeService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/registry")
    public ResponseEntity<UserRegistered> registry(@RequestBody final UserRegistryRecord user) {
        final UserRegistered userRegistered = userFacadeService.registry(user);

        return ResponseEntity.ok(userRegistered);
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registryWithSignIn(@RequestBody final UserRegistryRecord user) {
        final UserRegistered userRegistered = userFacadeService.registry(user);

        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(userRegistered.getEmail(), user.getPassword());
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);

        Object principal = authenticationResponse.getPrincipal();

        return ResponseEntity.ok(principal);
    }

    @GetMapping("/self")
    public ResponseEntity<Object> getSelf() {
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication());
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
