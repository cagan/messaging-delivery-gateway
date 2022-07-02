package com.cagan.messaginggateway.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("daily_message_quota")
@Data
public class DailyMessageQuota {
    @Id
    private String id;

    @Field("current_quota")
    private Integer currentQuota;

    @DBRef
    private User user;
}
