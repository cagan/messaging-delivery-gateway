package com.cagan.messaginggateway.rest.controller;

import com.cagan.messaginggateway.rest.dto.request.CreateAuthorizationTokenRequest;
import com.cagan.messaginggateway.rest.dto.response.CreateAuthorizationTokenResponse;
import com.cagan.messaginggateway.service.UserAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserAuthorizationService clientAuthorizationService;
//    @Qualifier("customAuthenticationManager")
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserAuthorizationService clientAuthorizationService, AuthenticationManager authenticationManager) {
        this.clientAuthorizationService = clientAuthorizationService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<CreateAuthorizationTokenResponse> authenticateUser(@Valid @RequestBody CreateAuthorizationTokenRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authToken = clientAuthorizationService.createNewClientAuthorizationToken(authentication.getPrincipal());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreateAuthorizationTokenResponse(authToken));
    }
}
