package com.cagan.messaginggateway.repository;

import com.cagan.messaginggateway.model.ClientAuthorization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientAuthorizationRepository extends MongoRepository<ClientAuthorization, String> {
    Optional<ClientAuthorization> findByAuthorizationToken(String authorizationToken);
}
