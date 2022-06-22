package com.cagan.messaginggateway;

import com.cagan.messaginggateway.config.GatewayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableConfigurationProperties(GatewayProperties.class)
public class MessaginggatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessaginggatewayApplication.class, args);
	}
}
