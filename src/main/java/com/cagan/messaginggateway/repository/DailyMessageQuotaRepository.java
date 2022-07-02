package com.cagan.messaginggateway.repository;

import com.cagan.messaginggateway.domain.DailyMessageQuota;
import com.cagan.messaginggateway.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DailyMessageQuotaRepository extends MongoRepository<DailyMessageQuota, String> {
    Optional<DailyMessageQuota> findByUser(User user);
}
