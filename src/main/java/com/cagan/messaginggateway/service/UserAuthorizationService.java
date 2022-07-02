package com.cagan.messaginggateway.service;

import com.cagan.messaginggateway.domain.UserAuthorization;
import com.cagan.messaginggateway.repository.UserAuthorizationRepository;
import com.cagan.messaginggateway.security.ClientUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Random;

@Service
@Transactional
public class UserAuthorizationService {
    private final UserAuthorizationRepository authorizationRepository;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    @Autowired
    public UserAuthorizationService(UserAuthorizationRepository authorizationRepository) {
        this.authorizationRepository = authorizationRepository;
    }

    public String createNewClientAuthorizationToken(Object principal) {
        UserAuthorization clientAuthorization = new UserAuthorization();
        ClientUserDetails clientUserDetails = (ClientUserDetails) principal;
        String token = generateToken();
        clientAuthorization.setAuthorizationToken(token);
        clientAuthorization.setUser(clientUserDetails.getUser());
        clientAuthorization.setExpireTime(Instant.now().plus(Duration.ofDays(1L))); // One day expire token
        authorizationRepository.save(clientAuthorization);
        return token;
    }

    private String generateToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
