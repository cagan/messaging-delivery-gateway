package com.cagan.messaginggateway.repository;

import com.cagan.messaginggateway.domain.User;
import com.cagan.messaginggateway.domain.UserAuthorization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthorizationRepository extends MongoRepository<UserAuthorization, String> {
    Optional<UserAuthorization> findByAuthorizationToken(String authorizationToken);

    Optional<UserAuthorization> findByUser(User user);

    Boolean existsByAuthorizationToken(String authorizationToken);
}
