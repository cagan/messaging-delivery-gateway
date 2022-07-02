package com.cagan.messaginggateway.rest.error;

import java.net.URI;

public class EmailAlreadyUsedException extends BadRequestAlertException {
    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super(URI.create("/api/v1/users"), "Username is already in use");
    }
}
