package by.sorface.sso.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2ClientController {

    @PostMapping("/oauth2/logout")
    public ResponseEntity<?> oauth2Logout() {
        return ResponseEntity.ok().build();
    }

}
