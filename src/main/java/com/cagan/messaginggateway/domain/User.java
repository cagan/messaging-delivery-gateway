package com.cagan.messaginggateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Set;


@Document("users")
@Data
public class User extends AuditMetaData {
    @Id
    private String id;

    @Field(name = "username")
    private String username;

    @Field(name = "messages")
    @DBRef
    @JsonIgnore
    private List<Message> messages;

    @Field("password")
    @JsonIgnore
    private String password;

    @Field("authorities")
    @JsonIgnore
    private Set<Authority> authorities;

    @Field("daily_message_quota")
    private Integer dailyMessageQuota;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + authorities +
                ", dailyMessageQuota=" + dailyMessageQuota +
                '}';
    }
}