package com.cagan.messaginggateway.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
public class AuditMetaData {

    @CreatedDate
    public Instant createDate;

    @LastModifiedDate
    public Instant lastModifiedDate;
}
