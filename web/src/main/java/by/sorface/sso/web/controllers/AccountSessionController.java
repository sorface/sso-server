package by.sorface.sso.web.controllers;

import by.sorface.sso.web.facade.session.AccountSessionFacade;
import by.sorface.sso.web.records.sessions.CleanupSession;
import by.sorface.sso.web.records.sessions.UserContextSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AccountSessionController {

    private final AccountSessionFacade accountSessionFacade;

    @GetMapping("/{username}")
    public UserContextSession findByUsername(@PathVariable final String username) {
        return accountSessionFacade.findByUsername(username);
    }

    @DeleteMapping
    public ResponseEntity<?> batchDelete(@RequestBody final CleanupSession cleanupSession) {
        accountSessionFacade.batchDelete(cleanupSession);

        return ResponseEntity.ok().build();
    }

}
