package by.sorface.sso.web.controllers;

import by.sorface.sso.web.exceptions.UnauthorizedException;
import by.sorface.sso.web.facade.signup.SignupEmailFacade;
import by.sorface.sso.web.facade.signup.SignupFacade;
import by.sorface.sso.web.records.principals.SfPrincipal;
import by.sorface.sso.web.records.requests.AccountSignup;
import by.sorface.sso.web.records.requests.ConfirmEmail;
import by.sorface.sso.web.records.requests.ResendConfirmEmail;
import by.sorface.sso.web.records.responses.AccountSignupResponse;
import by.sorface.sso.web.records.responses.ProfileRecord;
import by.sorface.sso.web.records.responses.UserConfirm;
import by.sorface.sso.web.records.responses.UserRegistered;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final SignupEmailFacade signupEmailFacade;

    private final SignupFacade signupFacade;

    @GetMapping("/current")
    public ProfileRecord getSelf() {
        final var auth = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(auth) || !auth.isAuthenticated()) {
            throw new UnauthorizedException("unauthorized");
        }

        final var principal = (SfPrincipal) auth.getPrincipal();

        return new ProfileRecord(principal.getId(), principal.getEmail(), principal.getFirstName(), principal.getLastName());
    }

    @PostMapping("/signup")
    public AccountSignupResponse signupWithSignIn(@RequestBody final AccountSignup userRegistry, final HttpServletRequest request) {
        signupEmailFacade.signup(userRegistry);

        try {
            request.login(userRegistry.email(), userRegistry.password());
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

        final var auth = (Authentication) request.getUserPrincipal();
        final var user = (SfPrincipal) auth.getPrincipal();

        return new AccountSignupResponse(user.getUsername(), userRegistry.email());
    }

    @PostMapping("/confirm")
    public UserConfirm confirm(@RequestBody final ConfirmEmail token) {
        return signupFacade.confirm(token);
    }

    @PostMapping("/confirm/resend")
    public UserRegistered resendConfirmEmail(@RequestBody final ResendConfirmEmail resendConfirmEmail) {
        return signupEmailFacade.resendConfirmEmail(resendConfirmEmail);
    }

}
