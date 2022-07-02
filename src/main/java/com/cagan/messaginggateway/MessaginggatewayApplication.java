package com.cagan.messaginggateway;

import com.cagan.messaginggateway.config.GatewayProperties;
import com.cagan.messaginggateway.domain.Authority;
import com.cagan.messaginggateway.domain.AuthorityType;
import com.cagan.messaginggateway.domain.User;
import com.cagan.messaginggateway.repository.AuthorityRepository;
import com.cagan.messaginggateway.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.TimeZone;

@Slf4j
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableConfigurationProperties({GatewayProperties.class})
public class MessaginggatewayApplication {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public MessaginggatewayApplication(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(MessaginggatewayApplication.class, args);
    }

    @Bean
    public CommandLineRunner createAdmin() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.out.println("TimeZone: " + TimeZone.getDefault());
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("password"));

                Authority authority = authorityRepository.findByName(AuthorityType.ROLE_ADMIN)
                        .orElseGet(() -> {
                            Authority adminAuth = new Authority();
                            adminAuth.setName(AuthorityType.ROLE_ADMIN);
                            authorityRepository.save(adminAuth);
                            return adminAuth;
                        });

                admin.setAuthorities(Set.of(authority));
                userRepository.save(admin);
                log.info("Admin user created");
            } else {
                log.info("Admin user already exists");
            }
        };
    }
}
