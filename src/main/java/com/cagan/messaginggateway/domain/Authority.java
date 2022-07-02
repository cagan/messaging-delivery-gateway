package com.cagan.messaginggateway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Document
public class Authority implements Serializable, GrantedAuthority {
    private static final long serialVersionUId = 1L;

    @Id
    private String id;

    @Field
    private AuthorityType name;

    @Override
    public String getAuthority() {
        return name.name();
    }
}
