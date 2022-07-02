package com.cagan.messaginggateway.rest.dto.response;


import com.cagan.messaginggateway.domain.Message;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties
@AllArgsConstructor
public class CreateUserResponse {
    @JsonProperty("client_name")
    private String clientName;

    @JsonProperty("messages")
    private List<Message> messages;
}
