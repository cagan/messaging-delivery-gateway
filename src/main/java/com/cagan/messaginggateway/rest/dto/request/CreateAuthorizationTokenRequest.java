package com.cagan.messaginggateway.rest.dto.request;

import com.cagan.messaginggateway.model.AuditMetaData;
import com.cagan.messaginggateway.model.AuthorityType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class CreateAuthorizationTokenRequest extends AuditMetaData {

    @JsonProperty("client_id")
    @NotEmpty(message = "client_id should be provided")
    private String clientId;

    @NotEmpty
    private Set<AuthorityType> authorities;
}
