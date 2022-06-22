package com.cagan.messaginggateway.rest.controller;

import com.cagan.messaginggateway.security.AuthTokenDetails;
import com.cagan.messaginggateway.security.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth/test")
public class AuthTestController {

    @GetMapping("/customer")
    @PreAuthorize("hasAuthority('SEND_MESSAGE')")
    public String getCustomer(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        log.info("User Details: {}", userDetails);
        return "Gateway Customer";
    }

    @PreAuthorize("hasAnyAuthority('SEND_MESSAGE', 'CANCEL_MESSAGE')")
    @GetMapping("/product")
    public String getProduct(@CurrentUser AuthTokenDetails currentUser) {
        log.info("Current User: {}", currentUser);
        return "Gateway Product";
    }
}
