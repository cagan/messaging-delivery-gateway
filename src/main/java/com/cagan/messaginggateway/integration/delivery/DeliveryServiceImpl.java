package com.cagan.messaginggateway.integration.delivery;

import com.cagan.messaginggateway.MessaginggatewayApplication;
import com.cagan.messaginggateway.domain.Message;
import com.cagan.messaginggateway.domain.MessageDeliveryRequestLog;
import com.cagan.messaginggateway.domain.MessageStatus;
import com.cagan.messaginggateway.domain.ResponseCodeType;
import com.cagan.messaginggateway.repository.MessageDeliveryRequestLogRepository;
import com.cagan.messaginggateway.service.MessageServiceCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    private static final Logger log = LoggerFactory.getLogger(MessaginggatewayApplication.class);
    private final MessageServiceCenter messageServiceCenter;
    private final MessageDeliveryRequestLogRepository messageLogRepository;

    @Autowired
    public DeliveryServiceImpl(MessageServiceCenter messageServiceCenter, MessageDeliveryRequestLogRepository messageLogRepository) {
        this.messageServiceCenter = messageServiceCenter;
        this.messageLogRepository = messageLogRepository;
    }

    @Override
    public void sendDeliveryToGSM(Message message, MessageDeliveryRequestLog messageLog) {
        int resultCode = messageServiceCenter.submitMessage(message.getOriginatingAddress(), messageLog.getRecipient(), message.getContent());

        if (resultCode != ResponseCodeType.SUCCESS) {
            throw new DeliveryFailedException("Something wrong with delivery");
        }

        messageLog.setStatus(MessageStatus.SUCCESS.value());
        messageLogRepository.save(messageLog);

        log.info("[STATUS: {}][MESSAGE_LOG: {}][MESSAGE: {}] SENT SUCCESSFULLY", MessageStatus.SUCCESS.value(), messageLog, message);
    }

    @Override
    public void recoverDelivery(DeliveryFailedException exception, Message message, MessageDeliveryRequestLog messageLog) {
        messageLog.setStatus(MessageStatus.FAILED.value());
        messageLogRepository.save(messageLog);

        log.info("[STATUS: {}][MESSAGE_LOG: {}] [MESSAGE: {}] COULD NOT SEND", MessageStatus.FAILED.value(), messageLog, message);
    }
}
