package com.cagan.messaginggateway.rest.error;

import java.net.URI;

public class InvalidExpirationTimeException extends BadRequestAlertException {
    public InvalidExpirationTimeException(String message) {
        super(URI.create("/api/v1/delivery/request"), message);
    }
}
