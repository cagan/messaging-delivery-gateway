package com.cagan.messaginggateway.service;

import com.cagan.messaginggateway.model.AuthorityType;
import com.cagan.messaginggateway.model.Client;
import com.cagan.messaginggateway.model.ClientAuthorization;
import com.cagan.messaginggateway.repository.ClientAuthorizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collections;
import java.util.Set;

@Service
@Transactional
public class ClientAuthorizationService {
    private final ClientAuthorizationRepository authorizationRepository;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    @Autowired
    public ClientAuthorizationService(ClientAuthorizationRepository authorizationRepository) {
        this.authorizationRepository = authorizationRepository;
    }

    public String createNewClientAuthorizationToken(Set<AuthorityType> authorities, Client client) {
        ClientAuthorization clientAuthorization = new ClientAuthorization();
        clientAuthorization.setClientId(client.getId());
        clientAuthorization.setAuthorities(authorities);

        String token = generateToken();
        clientAuthorization.setAuthorizationToken(token);
        authorizationRepository.save(clientAuthorization);
        return token;
    }

    public Set<AuthorityType> getAuthoritiesByAuthorizationToken(String authorizationToken) {
        return authorizationRepository.findByAuthorizationToken(authorizationToken)
                .map(ClientAuthorization::getAuthorities)
                .orElse(Collections.emptySet());
    }

    private String generateToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
