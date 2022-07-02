package com.cagan.messaginggateway.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document("user_authorization")
@Data
public class UserAuthorization {
    @Id
    private String id;

    @Field("authorization_token")
    private String authorizationToken;

    @DBRef
    private User user;

//    @Field("authorities")
//    private Set<AuthorityType> authorities;

    @Field("expire_time")
    private Instant expireTime;
}
