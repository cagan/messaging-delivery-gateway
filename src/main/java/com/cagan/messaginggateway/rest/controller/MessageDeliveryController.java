package com.cagan.messaginggateway.rest.controller;

import com.cagan.messaginggateway.domain.MessageDeliveryRequestLog;
import com.cagan.messaginggateway.rest.dto.request.MessageDeliveryRequest;
import com.cagan.messaginggateway.rest.dto.request.MessageQueryRequest;
import com.cagan.messaginggateway.rest.dto.response.MessageDeliveryResponse;
import com.cagan.messaginggateway.rest.dto.response.QueryResponse;
import com.cagan.messaginggateway.security.ClientUserDetails;
import com.cagan.messaginggateway.security.CurrentUser;
import com.cagan.messaginggateway.service.MessageQueryService;
import com.cagan.messaginggateway.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/messages")
@PreAuthorize("isAuthenticated()")
public class MessageDeliveryController {
    private final MessageService messageService;
    private final MessageQueryService messageQueryService;

    @Autowired
    public MessageDeliveryController(MessageService messageService, MessageQueryService messageQueryService) {
        this.messageService = messageService;
        this.messageQueryService = messageQueryService;
    }

    @PostMapping("/delivery/request")
    public ResponseEntity<MessageDeliveryResponse> sendMessageRequest(@AuthenticationPrincipal ClientUserDetails userDetails, @Valid @RequestBody MessageDeliveryRequest request) {
        log.info("CURRENT USER: {}", userDetails.getUser());
        String requestId = messageService.createMessageDeliveryRequest(userDetails.getUser(), request);
        return ResponseEntity.created(URI.create("/delivery/message/" + requestId)).body(new MessageDeliveryResponse(requestId));
    }

    @PutMapping("/cancel/{messageId}")
    public ResponseEntity<?> cancelMessageDelivery(@CurrentUser ClientUserDetails currentUser, @PathVariable String messageId) {
        Optional<List<MessageDeliveryRequestLog>> canceledMessageLogs = messageService.cancelMessage(messageId);

        if (canceledMessageLogs.isEmpty()) {
            throw new BadCredentialsException("Message not found");
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/search")
    public ResponseEntity<QueryResponse> search(@CurrentUser ClientUserDetails userDetails, @RequestBody MessageQueryRequest request) {
        log.info("start_time: {}", request.getStartTime());
        log.info("end_time: {}", request.getEndTime());
        log.info("UserDetails: {}", userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(messageQueryService.searchMessageRequests(request, userDetails.getUser()));
    }
}
