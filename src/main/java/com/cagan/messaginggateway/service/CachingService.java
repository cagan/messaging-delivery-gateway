package com.cagan.messaginggateway.service;

import com.cagan.messaginggateway.config.GatewayProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Service
public class CachingService {
    private final RedisTemplate<String, Object> template;
    private static final String HASH_KEY = "clients";
    private static final Logger log = LoggerFactory.getLogger(CachingService.class);
    private GatewayProperties properties;
    private long expireTime;


    public CachingService(RedisTemplate<String, Object> template, GatewayProperties properties) {
        this.template = template;
        this.properties = properties;
        this.setExpireTime();
    }

    public void put(String key, Object value) {
        log.info("PUT REDIS [KEY: {} VALUE: {}]", key, value);
//        template.expire(HASH_KEY, Duration.ofDays(1));
        template.expire(HASH_KEY, Duration.ofMillis(expireTime));
        template.opsForHash().put(key, HASH_KEY, value);
    }

    /**
     *
     * @param key of the redis value for search
     * @return <T> the return object converted from redis
     */
    public <T> T get(String key) {
        log.info("GET REDIS [KEY: {}]", key);
        return (T) template.opsForHash().get(HASH_KEY, key);
    }

    public void delete(String key) {
        log.info("DELETE REDIS [KEY: {}]", key);
        template.opsForHash().delete(key);
    }

    private void setExpireTime() {
//        Timestamp startTimeTimeStamp = Timestamp.valueOf(properties.getStartTime());
//        Timestamp endTimeTimeStamp = Timestamp.valueOf(properties.getEndTime());
//        long duration = (endTimeTimeStamp.getTime() - startTimeTimeStamp.getTime()) + startTimeTimeStamp.getTime();
//        Duration startTimeMillis = Duration.of(duration, ChronoUnit.MILLIS);
//        this.expireTime = startTimeMillis.toMillis();
    }
}
