package com.cagan.messaginggateway.security;

import com.cagan.messaginggateway.model.ClientAuthorization;
import com.cagan.messaginggateway.repository.ClientAuthorizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final AuthTokenUserDetailService userDetailService;
    private final ClientAuthorizationRepository authorizationRepository;

    @Autowired
    public CustomAuthenticationProvider(AuthTokenUserDetailService userDetailService, ClientAuthorizationRepository authorizationRepository) {
        this.userDetailService = userDetailService;
        this.authorizationRepository = authorizationRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String principal = (String) authentication.getPrincipal();
        UserDetails userDetails = userDetailService.loadUserByUsername(principal);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Optional<ClientAuthorization> clientAuthorization = authorizationRepository.findByAuthorizationToken(principal);

        if (clientAuthorization.isEmpty()) {
            throw new BadCredentialsException("The API key required");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, clientAuthorization.get().getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        authentication.setAuthenticated(true);
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
