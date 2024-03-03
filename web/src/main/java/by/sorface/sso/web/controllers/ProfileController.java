package by.sorface.sso.web.controllers;

import by.sorface.sso.web.records.SfPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    @GetMapping
    public Map<String, Object> getCurrentProfile() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final SfPrincipal principal = (SfPrincipal) authentication.getPrincipal();

        return Map.of(
                "id", principal.getId(),
                "email", principal.getEmail(),
                "firstName", principal.getFirstName(),
                "lastName", principal.getLastName()
        );
    }

}
