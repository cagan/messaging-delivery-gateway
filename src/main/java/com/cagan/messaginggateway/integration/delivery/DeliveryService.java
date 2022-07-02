package com.cagan.messaginggateway.integration.delivery;

import com.cagan.messaginggateway.domain.Message;
import com.cagan.messaginggateway.domain.MessageDeliveryRequestLog;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryService {
    long waitTime = 1000;
    int maxAttempts = 3;

    @Retryable(value = DeliveryFailedException.class, maxAttempts = maxAttempts, backoff = @Backoff(delay = waitTime))
    void sendDeliveryToGSM(Message message, MessageDeliveryRequestLog messageLog);

    @Recover
    void recoverDelivery(DeliveryFailedException exception, Message message, MessageDeliveryRequestLog messageLog);
}
