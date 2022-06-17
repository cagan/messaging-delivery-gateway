package com.cagan.messaginggateway.repository;

import com.cagan.messaginggateway.entity.Client;
import com.cagan.messaginggateway.entity.Message;
import com.cagan.messaginggateway.entity.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageLogRepository extends JpaRepository<MessageLog, Long> {

    List<MessageLog> findAllByStatusNotIn(List<String> status);

    MessageLog findByMessage(Message message);
}
