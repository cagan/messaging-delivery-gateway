package com.cagan.messaginggateway.rest.controller;

import com.cagan.messaginggateway.model.Client;
import com.cagan.messaginggateway.repository.ClientAuthorizationRepository;
import com.cagan.messaginggateway.rest.dto.request.CreateAuthorizationTokenRequest;
import com.cagan.messaginggateway.rest.dto.response.CreateAuthorizationTokenResponse;
import com.cagan.messaginggateway.service.ClientAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/client/token")
public class ClientAuthorizationController {
    private final ClientAuthorizationService clientAuthorizationService;

    @Autowired
    public ClientAuthorizationController(ClientAuthorizationService clientAuthorizationService) {
        this.clientAuthorizationService = clientAuthorizationService;
    }

    @PostMapping
    public ResponseEntity<CreateAuthorizationTokenResponse> createAuthorizationToken(@Valid @RequestBody CreateAuthorizationTokenRequest request) {
        Client client = new Client();
        client.setId(request.getClientId());

        String authToken = clientAuthorizationService.createNewClientAuthorizationToken(request.getAuthorities(), client);

        CreateAuthorizationTokenResponse response = new CreateAuthorizationTokenResponse();
        response.setClientId(client.getId());
        response.setAuthToken(authToken);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
