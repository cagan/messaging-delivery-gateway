package com.cagan.messaginggateway.service;

import com.cagan.messaginggateway.domain.Authority;
import com.cagan.messaginggateway.domain.AuthorityType;
import com.cagan.messaginggateway.domain.User;
import com.cagan.messaginggateway.repository.AuthorityRepository;
import com.cagan.messaginggateway.repository.UserRepository;
import com.cagan.messaginggateway.rest.dto.request.CreateUserRequest;
import com.cagan.messaginggateway.rest.dto.response.CreateUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository clientRepository;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final DailyMessageQuotaService dailyMessageQuotaService;

    @Autowired
    public UserService(UserRepository clientRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, DailyMessageQuotaService dailyMessageQuotaService) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.dailyMessageQuotaService = dailyMessageQuotaService;
    }

    public Optional<CreateUserResponse> createClient(CreateUserRequest request) {
        boolean clientExists = clientRepository.existsByUsername(request.getUsername());

        if (clientExists) {
            log.info("[USER: {}] username already exists.", request.getUsername());
            return Optional.empty();
        }

        User client = new User();
        client.setMessages(Collections.emptyList());
        client.setUsername(request.getUsername());
        client.setPassword(passwordEncoder.encode(request.getPassword()));
        client.setDailyMessageQuota(request.getDailyMessageQuota());

        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findByName(AuthorityType.ROLE_CLIENT).ifPresent(authorities::add);
        client.setAuthorities(authorities);

        clientRepository.save(client);
        log.info("New [USER: {}] created successfully.", client);

        dailyMessageQuotaService.createUserInitialDailyMessageQuota(client);

        return Optional.of(new CreateUserResponse(client.getUsername(), client.getMessages()));
    }
}
