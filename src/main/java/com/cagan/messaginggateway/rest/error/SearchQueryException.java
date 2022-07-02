package com.cagan.messaginggateway.rest.error;

import java.net.URI;

public class SearchQueryException extends BadRequestAlertException {
    private static final long serialVersionUID = 1L;

    public SearchQueryException(String message) {
        super(URI.create("/api/v1/messages/search/"), message);
    }
}
