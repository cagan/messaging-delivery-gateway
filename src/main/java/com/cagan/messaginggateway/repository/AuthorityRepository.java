package com.cagan.messaginggateway.repository;

import com.cagan.messaginggateway.domain.Authority;
import com.cagan.messaginggateway.domain.AuthorityType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthorityRepository extends MongoRepository<Authority, String> {
    Optional<Authority> findByName(AuthorityType name);

    Boolean existsByName(String name);
}
