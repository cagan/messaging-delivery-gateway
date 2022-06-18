package com.cagan.messaginggateway.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageStatus {
    TODO("TODO"), IN_PROGRESS("IN_PROGRESS"), FAILED("FAILED"), SUCCESS("SUCCESS");

    private String value;

    MessageStatus(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MessageStatus fromValue(String value) throws Exception {
        for (MessageStatus e : MessageStatus.values()) {
            if (e.value.equalsIgnoreCase(value)) {
                return e;
            }
        }

        throw new Exception("Message status not found");
    }

    @Override
    public String toString() {
        return this.value;
    }
}
