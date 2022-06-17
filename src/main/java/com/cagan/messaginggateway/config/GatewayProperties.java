package com.cagan.messaginggateway.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gateway")
@Data
public class GatewayProperties {
    private String startTime;
    private String endTime;

    private Redis redis = new Redis();

    @Getter
    @Setter
    public static class Redis {
        private String hostname;
        private String username;
        private String password;
    }
}
