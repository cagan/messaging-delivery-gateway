package com.cagan.messaginggateway.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateAuthorizationTokenResponse {
    @JsonProperty("auth_token")
    private String authToken;

    @JsonProperty("client_id")
    private String clientId;
}
