package by.sorface.sso.web.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class TemplateController {

    @GetMapping({"/account/sessions", "/account/signup", "/account", "/account/activate"})
    public String getUserPage() {
        return "index";
    }

    @GetMapping("/account/signin")
    public String getSigninPage(HttpServletResponse httpServletResponse) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication) && !(authentication instanceof AnonymousAuthenticationToken)) {
            httpServletResponse.sendRedirect("/account");
            httpServletResponse.setStatus(HttpServletResponse.SC_FOUND);
        }

        return "index";
    }

}
