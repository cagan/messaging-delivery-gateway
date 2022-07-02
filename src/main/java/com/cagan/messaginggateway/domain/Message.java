package com.cagan.messaginggateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
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

    @DBRef
    private User client;

    @Field("originating_address")
    private String originatingAddress;

    @Field("recipients")
    private List<String> recipients;

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", originatingAddress='" + originatingAddress + '\'' +
                ", recipients=" + recipients +
                '}';
    }
}
