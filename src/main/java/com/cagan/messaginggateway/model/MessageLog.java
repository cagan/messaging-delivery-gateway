package com.cagan.messaginggateway.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("message_logs")
@Data
public class MessageLog extends AuditMetaData {
    @Id
    private String id;

    @DBRef
    private Message message;

    @Field("status")
    private String status;

    @Field("recipient")
    private String recipient;
}
