package by.sorface.sso.web.controllers;

import by.sorface.sso.web.config.security.handlers.SavedRequestRedisSuccessHandler;
import by.sorface.sso.web.exceptions.UserRequestException;
import by.sorface.sso.web.facade.accounts.AccountFacade;
import by.sorface.sso.web.facade.signup.SignupEmailFacade;
import by.sorface.sso.web.facade.signup.SignupFacade;
import by.sorface.sso.web.records.principals.DefaultPrincipal;
import by.sorface.sso.web.records.requests.AccountSignup;
import by.sorface.sso.web.records.requests.ConfirmEmail;
import by.sorface.sso.web.records.requests.ResendConfirmEmail;
import by.sorface.sso.web.records.requests.UserPatchUpdate;
import by.sorface.sso.web.records.responses.AccountSignupResponse;
import by.sorface.sso.web.records.responses.ProfileRecord;
import by.sorface.sso.web.records.responses.UserConfirm;
import by.sorface.sso.web.records.responses.UserRegistered;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final SignupEmailFacade signupEmailFacade;

    private final SignupFacade signupFacade;

    private final AccountFacade accountFacade;

    private final SavedRequestRedisSuccessHandler savedRequestRedisSuccessHandler;

    @GetMapping("/current")
    public ProfileRecord getSelf() {
        final var principal = getCurrentUser();

        return accountFacade.getCurrent(principal.getId());
    }

    @PostMapping(
            value = "/signup",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AccountSignupResponse signupWithSignIn(final AccountSignup accountSignup,
                                                  final HttpServletRequest request,
                                                  final HttpServletResponse response) throws IOException, ServletException {
        signupEmailFacade.signup(accountSignup);

        try {
            request.login(accountSignup.email(), accountSignup.password());
        } catch (ServletException e) {
            throw new UserRequestException(e.getMessage());
        }

        final var auth = (Authentication) request.getUserPrincipal();
        final var user = (DefaultPrincipal) auth.getPrincipal();

        savedRequestRedisSuccessHandler.onAuthenticationSuccess(request, response, auth);

        return new AccountSignupResponse(user.getUsername(), accountSignup.email());
    }

    @PreAuthorize("#id == authentication.principal.id or hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody UserPatchUpdate userPatchUpdate, @PathVariable UUID id) {
        accountFacade.update(id, userPatchUpdate);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public UserConfirm confirm(@RequestBody final ConfirmEmail token) {
        return signupFacade.confirm(token);
    }

    @PostMapping("/confirm/resend")
    public UserRegistered resendConfirmEmail(@RequestBody final ResendConfirmEmail resendConfirmEmail) {
        return signupEmailFacade.resendConfirmEmail(resendConfirmEmail);
    }

    private DefaultPrincipal getCurrentUser() {
        final var auth = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(auth) || auth instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("exception.access.denied");
        }

        final var principal = (DefaultPrincipal) auth.getPrincipal();

        if (Objects.isNull(principal)) {
            throw new AccessDeniedException("exception.access.denied");
        }

        return principal;
    }
}
