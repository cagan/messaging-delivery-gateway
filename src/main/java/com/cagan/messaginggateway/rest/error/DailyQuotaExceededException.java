package com.cagan.messaginggateway.rest.error;

import java.net.URI;

public class DailyQuotaExceededException extends BadRequestAlertException {

    public DailyQuotaExceededException() {
        super(URI.create("/api/v1/delivery/request"), "Daily quota limit exceeded");
    }
}
