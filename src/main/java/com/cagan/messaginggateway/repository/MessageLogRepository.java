package com.cagan.messaginggateway.repository;

import com.cagan.messaginggateway.model.Message;
import com.cagan.messaginggateway.model.MessageLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageLogRepository extends MongoRepository<MessageLog, String> {
    List<MessageLog> findAllByStatusIn(List<String> statuses);

    List<MessageLog> findAllByMessage(Message message);
}
