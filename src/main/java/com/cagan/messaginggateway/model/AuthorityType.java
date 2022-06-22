package com.cagan.messaginggateway.model;

import org.springframework.security.core.GrantedAuthority;

public enum AuthorityType implements GrantedAuthority {
    SEND_MESSAGE, READ_MESSAGE, CANCEL_MESSAGE;

    @Override
    public String getAuthority() {
        return name();
    }
}
