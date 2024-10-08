package by.sorface.sso.web.controllers;

import by.sorface.sso.web.config.security.handlers.SavedRequestRedisSuccessHandler;
import by.sorface.sso.web.dao.nosql.redis.RedisOAuth2AuthorizationRepository;
import by.sorface.sso.web.exceptions.UserRequestException;
import by.sorface.sso.web.facade.accounts.AccountFacade;
import by.sorface.sso.web.facade.signup.SignupEmailFacade;
import by.sorface.sso.web.facade.signup.SignupFacade;
import by.sorface.sso.web.records.I18Codes;
import by.sorface.sso.web.records.principals.DefaultPrincipal;
import by.sorface.sso.web.records.requests.AccountSignup;
import by.sorface.sso.web.records.requests.ConfirmEmail;
import by.sorface.sso.web.records.requests.ResendConfirmEmail;
import by.sorface.sso.web.records.requests.UserPatchUpdate;
import by.sorface.sso.web.records.responses.AccountSignupResponse;
import by.sorface.sso.web.records.responses.ProfileRecord;
import by.sorface.sso.web.records.responses.UserConfirm;
import by.sorface.sso.web.records.responses.UserRegistered;
import by.sorface.sso.web.services.users.providers.ApplicationClientProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
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

    private final RedisOAuth2AuthorizationRepository repository;

    private final ApplicationClientProvider applicationClientProvider;

    @GetMapping("/current")
    public ProfileRecord getSelf() {
        final var principal = getCurrentUser();

        return accountFacade.getCurrent(principal.getId());
    }

    @PostMapping(value = "/signup")
    public AccountSignupResponse signupWithSignIn(@RequestBody final AccountSignup accountSignup,
                                                  final HttpServletRequest request,
                                                  final HttpServletResponse response) throws IOException, ServletException {
        signupEmailFacade.signup(accountSignup);

        try {
            request.login(accountSignup.email(), accountSignup.password());
        } catch (ServletException e) {
            throw new UserRequestException(I18Codes.I18UserCodes.ALREADY_AUTHENTICATED);
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

    @PostMapping("/locale")
    public ResponseEntity<?> changeLocale(@RequestParam String lang) {
        return ResponseEntity.ok(
                Map.of("language", LocaleContextHolder.getLocale())
        );
    }

    @PostMapping("/confirm/resend")
    public UserRegistered resendConfirmEmail(@RequestBody final ResendConfirmEmail resendConfirmEmail) {
        return signupEmailFacade.resendConfirmEmail(resendConfirmEmail);
    }

    private DefaultPrincipal getCurrentUser() {
        final var auth = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(auth) || auth instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException(I18Codes.I18GlobalCodes.ACCESS_DENIED);
        }

        final var principal = (DefaultPrincipal) auth.getPrincipal();

        if (Objects.isNull(principal)) {
            throw new AccessDeniedException(I18Codes.I18GlobalCodes.ACCESS_DENIED);
        }

        return principal;
    }
}
