package com.cagan.messaginggateway.util;

import org.bson.json.StrictJsonWriter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.LocalDateTime;
import java.time.ZoneId;

@WritingConverter
public class LocalDateTimeConverter implements Converter<LocalDateTime, Long> {

    @Override
    public Long convert(LocalDateTime source) {
        long timestamp;
        timestamp = source.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return timestamp;
    }
}
