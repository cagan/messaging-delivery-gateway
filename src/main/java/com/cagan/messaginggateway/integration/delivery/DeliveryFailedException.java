package com.cagan.messaginggateway.integration.delivery;

import lombok.Data;

public class DeliveryFailedException extends RuntimeException {
    private final String message;

    public DeliveryFailedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
