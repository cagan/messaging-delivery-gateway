package com.cagan.messaginggateway.task;

import com.cagan.messaginggateway.domain.Message;
import com.cagan.messaginggateway.domain.MessageStatus;
import com.cagan.messaginggateway.integration.delivery.DeliveryService;
import com.cagan.messaginggateway.domain.MessageDeliveryRequestLog;
import com.cagan.messaginggateway.repository.MessageDeliveryRequestLogRepository;
import com.cagan.messaginggateway.repository.MessageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Component
public class DeliveryMessageTask {
    private final MessageDeliveryRequestLogRepository messageLogRepository;
    private final MessageRepository messageRepository;
    private final DeliveryService deliveryService;

    @Scheduled(cron = "${tasks.delivery-message}", zone = "Europe/Istanbul")
    public void deliverMessagesJob() {
        List<MessageDeliveryRequestLog> messageLogs = messageLogRepository
                .findAllByStatusInAndExpirationTimeAfter(
                        List.of(MessageStatus.TODO.getValue(), MessageStatus.FAILED.value()), Instant.now()
                );

        if (messageLogs.isEmpty()) {
            log.info("No Message found to send");
            return;
        }

        for (MessageDeliveryRequestLog messageLog : messageLogs) {
            Optional<Message> message = messageRepository.findById(messageLog.getMessage().getId());

            if (message.isEmpty()) {
                log.warn("Message does not exists with id: {}", messageLog.getMessage().getId());
                continue;
            }
            Message foundMessage = message.get();
            deliveryService.sendDeliveryToGSM(foundMessage, messageLog);
        }
    }
}
