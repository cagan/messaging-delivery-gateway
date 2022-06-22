package com.cagan.messaginggateway.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Document("client_authorization")
@Data
public class ClientAuthorization {
    @Id
    private String id;

    @Field("authorization_token")
    private String authorizationToken;

    @Field("client_id")
    private String clientId;

    @Field("authorities")
    private Set<AuthorityType> authorities;
}
