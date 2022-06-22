package com.cagan.messaginggateway.security;

import com.cagan.messaginggateway.model.AuthorityType;
import com.cagan.messaginggateway.model.ClientAuthorization;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthTokenDetails implements UserDetails {
    private final ClientAuthorization clientAuthorization;

    public AuthTokenDetails(ClientAuthorization clientAuthorization) {
        this.clientAuthorization = clientAuthorization;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.clientAuthorization.getAuthorities();
    }

    public String getAuthToken() {
        return clientAuthorization.getAuthorizationToken();
    }

    public String getClientId() {
        return clientAuthorization.getClientId();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
