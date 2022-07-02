package com.cagan.messaginggateway.rest.error;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

import java.net.URI;

public class BadRequestAlertException extends AbstractThrowableProblem {
    private static final long serialVersionUID = 1L;

    public BadRequestAlertException(URI uri, String message) {
        super(uri, message, Status.BAD_REQUEST);
    }
}
