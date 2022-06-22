package com.cagan.messaginggateway.repository;

import com.cagan.messaginggateway.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {
    Optional<Client> findByUsername(String clientName);
    Boolean existsByUsername(String clientName);
}
