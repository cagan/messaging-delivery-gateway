package com.cagan.messaginggateway.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.List;

@Data
public class MessageDeliveryRequest implements Serializable {
    private static final long serialVersionUID = 3487495895819393L;

    @NotNull
    @NotBlank
    private String originatingAddress;

    @NotNull
    @Size(max = 1024)
    private String content;

    @NotNull
    @Size(min = 1, max = 10)
    private List<String> recipients;

    private Instant expirationTime;
}
