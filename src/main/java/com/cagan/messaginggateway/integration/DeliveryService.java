package com.cagan.messaginggateway.integration;

import com.cagan.messaginggateway.model.Message;
import com.cagan.messaginggateway.model.MessageLog;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryService {
    long waitTime = 1000;
    int maxAttempts = 3;

    @Retryable(value = DeliveryFailedException.class, maxAttempts = maxAttempts, backoff = @Backoff(delay = waitTime))
    void sendDeliveryToGSM(Message message, MessageLog messageLog);

    @Recover
    void recoverDelivery(DeliveryFailedException exception, Message message, MessageLog messageLog);
}
