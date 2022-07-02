package com.cagan.messaginggateway.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document("message_delivery_request_log")
@Data
public class MessageDeliveryRequestLog extends AuditMetaData {
    @Id
    private String id;

    @DBRef
    private Message message;

    @DBRef
    private User user;

    @Field("status")
    private String status;

    @Field("recipient")
    private String recipient;

    @Field("expiration_time")
    private Instant expirationTime;
}
