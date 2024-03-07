package by.sorface.sso.web.controllers;

import by.sorface.sso.web.exceptions.UnauthorizedException;
import by.sorface.sso.web.records.SfPrincipal;
import by.sorface.sso.web.records.responses.ProfileRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    @GetMapping
    public ProfileRecord getCurrentProfile() {
        final var auth = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(auth)) {
            throw new UnauthorizedException("unauthorized");
        }

        final var principal = (SfPrincipal) auth.getPrincipal();

        return new ProfileRecord(principal.getId(), principal.getEmail(), principal.getFirstName(), principal.getLastName());
    }

}
