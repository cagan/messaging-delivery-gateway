package com.cagan.messaginggateway.security;

import com.cagan.messaginggateway.model.ClientAuthorization;
import com.cagan.messaginggateway.repository.ClientAuthorizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {
    private final ClientAuthorizationRepository authorizationRepository;
    private final AuthTokenUserDetailService authTokenUserDetailService;

    @Autowired
    public CustomAuthenticationManager(ClientAuthorizationRepository authorizationRepository, AuthTokenUserDetailService authTokenUserDetailService) {
        this.authorizationRepository = authorizationRepository;
        this.authTokenUserDetailService = authTokenUserDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String principal = (String)authentication.getPrincipal(); // Token
        Optional<ClientAuthorization> clientAuthorization = authorizationRepository.findByAuthorizationToken(principal);

        if (clientAuthorization.isEmpty()) {
            throw new BadCredentialsException("The API key required");
        }

        UserDetails userDetails = authTokenUserDetailService.loadUserByUsername(principal);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, clientAuthorization.get().getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authenticationToken;
    }
}
