package com.cagan.messaginggateway.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document("messages")
@Data
public class Message extends AuditMetaData {
    @Id
    private String id;

    @Field("content")
    private String content;

    @Field("client_id")
    private String clientId;

    @Field("originating_address")
    private String originatingAddress;

    @Field("recipients")
    private List<String> recipients;
}
