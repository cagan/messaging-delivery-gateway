package com.cagan.messaginggateway.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserRequest {

    @NotNull
    @JsonProperty("username")
    private String username;

    @NotNull
    @Min(1)
    @Max(10)
    @JsonProperty(value = "daily_message_quota", namespace = "daily message quota required")
    private Integer dailyMessageQuota;

    @NotNull
    private String password;
}
