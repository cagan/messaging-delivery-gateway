package com.cagan.messaginggateway.rest.dto.response;

import com.cagan.messaginggateway.config.LocalDateTimeDeserializer;
import com.cagan.messaginggateway.config.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonPropertyOrder({"count", "start_time", "end_time", "success", "failed"})
public class QueryResponse {
    @JsonProperty("count")
    Long count;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonProperty("start_time")
    LocalDateTime startTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonProperty("end_time")
    LocalDateTime endTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("success")
    Boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("failed")
    Boolean failed;
}
