package com.cagan.messaginggateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.validation.ConstraintViolationProblemModule;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModules(
                new ProblemModule(),
                new ConstraintViolationProblemModule(),
                new JavaTimeModule()
        );
    }
}
