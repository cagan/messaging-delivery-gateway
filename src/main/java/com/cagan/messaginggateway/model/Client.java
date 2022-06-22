package com.cagan.messaginggateway.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.userdetails.UserDetails;


@Document("clients")
@Data
public class Client extends AuditMetaData {
    @Id
    private String id;

    @Field(name = "username")
    private String username;
}
