package com.cagan.messaginggateway.rest.controller;

import com.cagan.messaginggateway.rest.dto.request.CreateUserRequest;
import com.cagan.messaginggateway.rest.dto.response.CreateUserResponse;
import com.cagan.messaginggateway.rest.error.EmailAlreadyUsedException;
import com.cagan.messaginggateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService clientService) {
        this.userService = clientService;
    }

    @PostMapping
    //TODO: only admin user can add new client user
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CreateUserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        return userService.createClient(request)
                .map(createClientResponse -> ResponseEntity.status(HttpStatus.CREATED).body(createClientResponse))
                .orElseThrow(EmailAlreadyUsedException::new);
    }
}
