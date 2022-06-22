package com.cagan.messaginggateway.security;

import com.cagan.messaginggateway.model.ClientAuthorization;
import com.cagan.messaginggateway.repository.ClientAuthorizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("userDetailsService")
public class AuthTokenUserDetailService implements UserDetailsService {
    private final ClientAuthorizationRepository clientAuthorizationRepository;

    @Autowired
    public AuthTokenUserDetailService(ClientAuthorizationRepository clientAuthorizationRepository) {
        this.clientAuthorizationRepository = clientAuthorizationRepository;
    }

    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        ClientAuthorization clientAuthorization = clientAuthorizationRepository.findByAuthorizationToken(token)
                .orElse(null);

        return new AuthTokenDetails(clientAuthorization);
    }
}
