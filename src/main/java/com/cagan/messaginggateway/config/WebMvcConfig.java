package com.cagan.messaginggateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public FormattingConversionService mvcConversionService1() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

        var dateTimeRegistrar = new DateTimeFormatterRegistrar();
        dateTimeRegistrar.setDateTimeFormatter(DateTimeFormatter.ISO_INSTANT);
        dateTimeRegistrar.setDateFormatter(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        dateTimeRegistrar.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm:ss"));
        dateTimeRegistrar.setDateStyle(FormatStyle.LONG);
        dateTimeRegistrar.setTimeStyle(FormatStyle.FULL);

        dateTimeRegistrar.setUseIsoFormat(true);
        dateTimeRegistrar.registerFormatters(conversionService);

        return conversionService;
    }

//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
//    }
}
