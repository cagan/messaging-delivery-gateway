package com.cagan.messaginggateway.rest.controller;

import com.cagan.messaginggateway.model.MessageLog;
import com.cagan.messaginggateway.rest.dto.request.MessageDeliveryRequest;
import com.cagan.messaginggateway.rest.dto.response.MessageDeliveryResponse;
import com.cagan.messaginggateway.security.AuthTokenDetails;
import com.cagan.messaginggateway.security.CurrentUser;
import com.cagan.messaginggateway.security.IsViewer;
import com.cagan.messaginggateway.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageDeliveryController {
    private final MessageService messageService;

    @Autowired
    public MessageDeliveryController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/delivery/request")
    @PreAuthorize("hasAuthority('SEND_MESSAGE')")
    @IsViewer
    public ResponseEntity<MessageDeliveryResponse> sendMessageRequest(@CurrentUser AuthTokenDetails currentUser, @Valid @RequestBody MessageDeliveryRequest request) {
        String requestId = messageService.createMessageDeliveryRequest(request);
        return ResponseEntity.created(URI.create("/delivery/message/" + requestId)).body(new MessageDeliveryResponse(requestId));
    }

    @PutMapping("/cancel/{messageId}")
    @PreAuthorize("hasAuthority('CANCEL_MESSAGE')")
    public ResponseEntity<?> cancelMessageDelivery(@CurrentUser AuthTokenDetails currentUser, @PathVariable String messageId) {

        Optional<List<MessageLog>> canceledMessageLogs = messageService.cancelMessage(messageId);

        if (canceledMessageLogs.isEmpty()) {
            throw new BadCredentialsException("Message not found");
        }

        return ResponseEntity.noContent().build();
    }
}
