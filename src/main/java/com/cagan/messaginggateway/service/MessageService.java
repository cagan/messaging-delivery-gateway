package com.cagan.messaginggateway.service;

import com.cagan.messaginggateway.integration.DeliveryService;
import com.cagan.messaginggateway.model.Client;
import com.cagan.messaginggateway.model.Message;
import com.cagan.messaginggateway.model.MessageLog;
import com.cagan.messaginggateway.model.MessageStatus;
import com.cagan.messaginggateway.repository.MessageLogRepository;
import com.cagan.messaginggateway.repository.MessageRepository;
import com.cagan.messaginggateway.rest.dto.request.MessageDeliveryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    private final MessageRepository messageRepository;
    private final MessageLogRepository messageLogRepository;
    private final DeliveryService deliveryService;

    @Autowired
    public MessageService(MessageRepository messageRepository, MessageLogRepository messageLogRepository, DeliveryService deliveryService) {
        this.messageRepository = messageRepository;
        this.messageLogRepository = messageLogRepository;
        this.deliveryService = deliveryService;
    }

    public String createMessageDeliveryRequest(MessageDeliveryRequest request) {
        Message message = new Message();
        message.setOriginatingAddress(request.getOriginatingAddress());
        message.setContent(request.getContent());
        message.setRecipients(request.getRecipients());
        messageRepository.save(message);

        request.getRecipients().forEach(recipient -> {
            MessageLog messageLog = new MessageLog();
            messageLog.setMessage(message);
            messageLog.setStatus(MessageStatus.TODO.getValue());
            messageLog.setRecipient(recipient);
            messageLogRepository.save(messageLog);
        });

        return message.getId();
    }

    @Scheduled(cron = "${cron.expression}", zone = "Europe/Istanbul")
    public void deliverMessagesJob() {
        List<MessageLog> messageLogs = messageLogRepository.findAllByStatusIn(List.of(MessageStatus.TODO.getValue(), MessageStatus.FAILED.value()));

        if (messageLogs.isEmpty()) {
            log.info("No Message found to send");
            return;
        }

        for (MessageLog messageLog : messageLogs) {
            Optional<Message> message = messageRepository.findById(messageLog.getMessage().getId());

            if (message.isEmpty()) {
                log.warn("Message does not exists with id: {}", messageLog.getMessage().getId());
                continue;
            }
            Message foundMessage = message.get();
            deliveryService.sendDeliveryToGSM(foundMessage, messageLog);
        }
    }

    public Optional<List<MessageLog>> cancelMessage(String messageId) {
        Optional<Message> message = messageRepository.findById(messageId);

        if (message.isEmpty()) {
            return Optional.empty();
        }

        Message foundMessage = message.get();

        List<MessageLog> canceledMessageLogs = messageLogRepository.findAllByMessage(foundMessage)
                .stream().peek(messageLog -> {
                    messageLog.setStatus(MessageStatus.CANCELED.value());
                    log.info("[MESSAGE_LOG: {}] changed status to : [STATUS: {}] for [MESSAGE: {}]", messageLog, MessageStatus.CANCELED.value(), foundMessage);
                }).toList();

        messageLogRepository.saveAll(canceledMessageLogs);

        return Optional.of(canceledMessageLogs);
    }
}
