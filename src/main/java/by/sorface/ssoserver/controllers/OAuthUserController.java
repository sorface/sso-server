package by.sorface.ssoserver.controllers;

import by.sorface.ssoserver.records.SorfaceUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/api")
public class OAuthUserController {

    @GetMapping("/self")
    public SorfaceUser GetSelf() {
        return (SorfaceUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
