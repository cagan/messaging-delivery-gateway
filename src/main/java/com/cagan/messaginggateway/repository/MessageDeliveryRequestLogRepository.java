package com.cagan.messaginggateway.repository;

import com.cagan.messaginggateway.domain.Message;
import com.cagan.messaginggateway.domain.MessageDeliveryRequestLog;
import com.cagan.messaginggateway.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageDeliveryRequestLogRepository extends MongoRepository<MessageDeliveryRequestLog, String> {
    List<MessageDeliveryRequestLog> findAllByStatusIn(List<String> statuses);
    List<MessageDeliveryRequestLog> findAllByStatusInAndExpirationTimeAfter(List<String> statuses, Instant expirationTime);

    List<MessageDeliveryRequestLog> findAllByMessage(Message message);

    Long countByCreateDateBetweenAndUser(LocalDateTime startTime, LocalDateTime endTime, User user);
}
