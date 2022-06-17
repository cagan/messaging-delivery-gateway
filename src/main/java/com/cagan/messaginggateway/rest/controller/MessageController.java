package com.cagan.messaginggateway.rest.controller;

import com.cagan.messaginggateway.rest.dto.request.MessageDeliveryRequest;
import com.cagan.messaginggateway.rest.dto.response.MessageDeliveryResponse;
import com.cagan.messaginggateway.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/delivery/request")
    public ResponseEntity<MessageDeliveryResponse> sendMessageRequest(@Valid @RequestBody MessageDeliveryRequest request) throws IOException {
        System.out.println(request.getContent());

        String requestId = messageService.forwardMessage(request);

        return ResponseEntity.created(URI.create("/delivery/message/" + requestId)).body(new MessageDeliveryResponse(requestId));
    }
}
