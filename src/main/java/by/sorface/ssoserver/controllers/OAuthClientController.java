package by.sorface.ssoserver.controllers;

import by.sorface.ssoserver.facade.OAuthClientFacade;
import by.sorface.ssoserver.records.OAuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth/clients")
@RequiredArgsConstructor
public class OAuthClientController {

    private final OAuthClientFacade oAuthClientFacade;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody final OAuthClient oAuthClient) {
        oAuthClientFacade.create(oAuthClient);

        return ResponseEntity.ok().build();
    }

}
