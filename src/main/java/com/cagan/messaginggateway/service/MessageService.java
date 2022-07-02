package com.cagan.messaginggateway.service;

import com.cagan.messaginggateway.domain.Message;
import com.cagan.messaginggateway.domain.MessageStatus;
import com.cagan.messaginggateway.domain.User;
import com.cagan.messaginggateway.domain.MessageDeliveryRequestLog;
import com.cagan.messaginggateway.repository.MessageDeliveryRequestLogRepository;
import com.cagan.messaginggateway.repository.MessageRepository;
import com.cagan.messaginggateway.rest.dto.request.MessageDeliveryRequest;
import com.cagan.messaginggateway.rest.error.DailyQuotaExceededException;
import com.cagan.messaginggateway.rest.error.InvalidExpirationTimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    private final MessageRepository messageRepository;
    private final MessageDeliveryRequestLogRepository messageDeliveryRequestRepository;
    private final DailyMessageQuotaService dailyMessageQuotaService;

    @Autowired
    public MessageService(MessageRepository messageRepository, MessageDeliveryRequestLogRepository messageLogRepository, DailyMessageQuotaService dailyMessageQuotaService) {
        this.messageRepository = messageRepository;
        this.messageDeliveryRequestRepository = messageLogRepository;
        this.dailyMessageQuotaService = dailyMessageQuotaService;
    }

    public String createMessageDeliveryRequest(User user, MessageDeliveryRequest request) {
        if(dailyMessageQuotaService.isDailyQuotaExceeded(user)) {
            log.info("Daily quota limit exceeded for [USER: {}]", user);
            throw new DailyQuotaExceededException();
        }

        Message message = createMessage(user, request);
        log.info("[MESSAGE: {}] saved", message.getId());

        List<MessageDeliveryRequestLog> messageDeliveryRequestLogs = createMessageRequestLogs(message, user, request.getExpirationTime());
        dailyMessageQuotaService.incrementQuotaUsage(user);

        return message.getId();
    }

    protected Message createMessage(User user, MessageDeliveryRequest request) {
        Message message = new Message();
        message.setOriginatingAddress(request.getOriginatingAddress());
        message.setContent(request.getContent());
        message.setRecipients(request.getRecipients());
        message.setClient(user);
        return messageRepository.save(message);
    }

    protected List<MessageDeliveryRequestLog> createMessageRequestLogs(Message message, User user, Instant expirationTime) {
        if (expirationTime.compareTo(Instant.now().plus(Duration.ofHours(1))) < 0) {
            throw new InvalidExpirationTimeException("Expiration time can not be less then current time");
        }

        List<MessageDeliveryRequestLog> messageDeliveryRequestLogs = message.getRecipients().stream().map(recipient -> {
            MessageDeliveryRequestLog messageDeliveryRequest = new MessageDeliveryRequestLog();
            messageDeliveryRequest.setExpirationTime(expirationTime.plus(Duration.ofDays(1)));
            messageDeliveryRequest.setMessage(message);
            messageDeliveryRequest.setRecipient(recipient);
            messageDeliveryRequest.setStatus(MessageStatus.TODO.value());
            messageDeliveryRequest.setUser(user);

            log.info("[MESSAGE_DELIVERY_REQUEST: {}] saved", messageDeliveryRequest);
            return messageDeliveryRequest;
        }).toList();

        messageDeliveryRequestRepository.saveAll(messageDeliveryRequestLogs);
        return messageDeliveryRequestLogs;
    }

    public Optional<List<MessageDeliveryRequestLog>> cancelMessage(String messageId) {
        Optional<Message> message = messageRepository.findById(messageId);

        if (message.isEmpty()) {
            return Optional.empty();
        }

        Message foundMessage = message.get();

        List<MessageDeliveryRequestLog> canceledMessageLogs = messageDeliveryRequestRepository.findAllByMessage(foundMessage)
                .stream().peek(messageLog -> {
                    messageLog.setStatus(MessageStatus.CANCELED.value());
                    log.info("[MESSAGE_LOG: {}] changed status to : [STATUS: {}] for [MESSAGE: {}]", messageLog, MessageStatus.CANCELED.value(), foundMessage);
                }).toList();

        messageDeliveryRequestRepository.saveAll(canceledMessageLogs);

        return Optional.of(canceledMessageLogs);
    }
}
