package com.cagan.messaginggateway.integration;

import com.cagan.messaginggateway.MessaginggatewayApplication;
import com.cagan.messaginggateway.model.Message;
import com.cagan.messaginggateway.model.MessageLog;
import com.cagan.messaginggateway.model.MessageStatus;
import com.cagan.messaginggateway.model.ResponseCodeType;
import com.cagan.messaginggateway.repository.MessageLogRepository;
import com.cagan.messaginggateway.service.MessageServiceCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    private static final Logger log = LoggerFactory.getLogger(MessaginggatewayApplication.class);
    private final MessageServiceCenter messageServiceCenter;
    private final MessageLogRepository messageLogRepository;

    @Autowired
    public DeliveryServiceImpl(MessageServiceCenter messageServiceCenter, MessageLogRepository messageLogRepository) {
        this.messageServiceCenter = messageServiceCenter;
        this.messageLogRepository = messageLogRepository;
    }

    @Override
    public void sendDeliveryToGSM(Message message, MessageLog messageLog) {
        int resultCode = messageServiceCenter.submitMessage(message.getOriginatingAddress(), messageLog.getRecipient(), message.getContent());

        if (resultCode != ResponseCodeType.SUCCESS) {
            throw new DeliveryFailedException("Something wrong with delivery");
        }

        messageLog.setStatus(MessageStatus.SUCCESS.value());
        messageLogRepository.save(messageLog);

        log.info("[STATUS: {}][MESSAGE_LOG: {}][MESSAGE: {}] SENT SUCCESSFULLY", MessageStatus.SUCCESS.value(), messageLog, message);
    }

    @Override
    public void recover(DeliveryFailedException exception, Message message, MessageLog messageLog) {
        messageLog.setStatus(MessageStatus.FAILED.value());
        messageLogRepository.save(messageLog);

        log.info("[STATUS: {}][MESSAGE_LOG: {}] [MESSAGE: {}] COULD NOT SEND", MessageStatus.FAILED.value(), messageLog, message);
    }
}
