package by.sorface.sso.web.controllers;

import by.sorface.sso.web.facade.clients.ApplicationClientFacade;
import by.sorface.sso.web.records.requests.ApplicationClientDelete;
import by.sorface.sso.web.records.requests.ApplicationRegistry;
import by.sorface.sso.web.records.responses.ApplicationClient;
import by.sorface.sso.web.records.responses.ApplicationClientRefreshSecret;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationClientController {

    private final ApplicationClientFacade applicationClientFacade;

    @GetMapping
    public List<ApplicationClient> findAllForCurrent() {
        return applicationClientFacade.findByCurrentUser();
    }

    @GetMapping("/{clientId}")
    public ApplicationClient getById(@PathVariable("clientId") final UUID clientId) {
        return applicationClientFacade.getByIdAndCurrentUser(clientId);
    }

    @PatchMapping("/{clientId}/refresh")
    public ApplicationClientRefreshSecret refreshSecret(@PathVariable("clientId") final UUID clientId) {
        return applicationClientFacade.refreshSecret(clientId);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody final ApplicationClientDelete applicationClientDelete) {
        applicationClientFacade.delete(applicationClientDelete);

        return ResponseEntity.ok().build();
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ApplicationClient registry(@RequestBody @Valid final ApplicationRegistry applicationRegistry) {
        return applicationClientFacade.registry(applicationRegistry);
    }

}
