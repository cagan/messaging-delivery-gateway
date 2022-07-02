package com.cagan.messaginggateway.rest.dto.request;

import com.cagan.messaginggateway.config.LocalDateTimeDeserializer;
import com.cagan.messaginggateway.config.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MessageQueryRequest implements Serializable {
    private static final long serialVersionID = 1L;

    @JsonProperty("start_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    private boolean success;

    private boolean failed;

    @Override
    public String toString() {
        return "MessageQueryRequest{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageQueryRequest)) return false;
        MessageQueryRequest that = (MessageQueryRequest) o;
        return Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }
}
