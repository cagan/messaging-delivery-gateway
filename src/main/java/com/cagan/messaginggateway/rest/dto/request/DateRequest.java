package com.cagan.messaginggateway.rest.dto.request;


import com.cagan.messaginggateway.config.LocalDateTimeDeserializer;
import com.cagan.messaginggateway.config.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DateRequest implements Serializable {
    protected static final long serialVersionUID = -2118915787869082064L;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("local_date_time")
    protected LocalDateTime localDateTime;
}
