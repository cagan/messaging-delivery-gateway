package com.cagan.messaginggateway.rest.controller;

import com.cagan.messaginggateway.config.LocalDateTimeDeserializer;
import com.cagan.messaginggateway.rest.dto.request.DateRequest;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
public class DateController {
    @PostMapping("/date")
    public Date date(@RequestParam("date") Date date) {
        System.out.println(date);
        return date;
    }

    @PostMapping("/localdate")
    public LocalDate localDate(@RequestParam("localdate") LocalDate localDate) {
        System.out.println(localDate);
        return localDate;
    }

    @PostMapping("/localdatetime")
    public LocalDateTime localDateTime(@RequestBody DateRequest request) {
        System.out.println(request.getLocalDateTime());
        return request.getLocalDateTime();
    }
}
