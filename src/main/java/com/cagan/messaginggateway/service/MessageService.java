package com.cagan.messaginggateway.service;

import com.cagan.messaginggateway.entity.Client;
import com.cagan.messaginggateway.entity.Message;
import com.cagan.messaginggateway.entity.MessageLog;
import com.cagan.messaginggateway.repository.ClientRepository;
import com.cagan.messaginggateway.repository.MessageLogRepository;
import com.cagan.messaginggateway.repository.MessageRepository;
import com.cagan.messaginggateway.rest.dto.request.MessageDeliveryRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
@Transactional
public class MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    private final CachingService cachingService;
    private final MessageLogRepository messageLogRepository;
    private final MessageRepository messageRepository;
    private final ClientRepository clientRepository;

    public String forwardMessage(MessageDeliveryRequest request) throws JsonProcessingException {
//        cachingService.put(request.getOriginatingAddress(), request);
//        MessageDeliveryRequest request1 = cachingService.get(request.getOriginatingAddress());
//        log.info("ADDRESS: [{}]", request1.getOriginatingAddress());
//        log.info("CONTENT: [{}]", request1.getContent());
        Client client = new Client();
        client.setName("cagan");
        clientRepository.save(client);

        Message message = new Message();
        message.setContent(request.getContent());
        message.setOriginatingAddress(request.getOriginatingAddress());
        message.setClient(client);
        ObjectMapper om = new ObjectMapper();
        String recipientsJson = om.writer().writeValueAsString(request.getRecipients());
        message.setRecipients(recipientsJson);
        messageRepository.save(message);

        MessageLog messageLog = new MessageLog();
        messageLog.setMessage(message);
        messageLog.setStatus("TODO");
        messageLog.setClient(client);
        messageLogRepository.save(messageLog);

        processMessagesScheduler();

        return null;
    }

//    @Scheduled(cron = "0 0/1 ${gateway.start-time}-${gateway.end-time} * * ?", zone = "Europe/Istanbul")
//    @Scheduled(fixedRate = 2000)
    private void processMessagesScheduler() throws JsonProcessingException {
        List<MessageLog> messageLogs =  messageLogRepository.findAllByStatusNotIn(List.of("DONE", "IN_PROGRESS"));

        for (MessageLog messageLog : messageLogs) {
            messageLog.setStatus("IN_PROGRESS");
            messageLogRepository.save(messageLog);
            Message message = messageLog.getMessage();
            sendToMessageCenter(message);
            log.info("[Message: {}] has been processed: ", message.getId());
        }
    }

    public void sendToMessageCenter(Message message) throws JsonProcessingException {
        MessageServiceCenter messageServiceCenter = new MessageServiceCenter();
        int requestThreshold = 3;
        AtomicInteger current = new AtomicInteger();

        while (current.get() < requestThreshold) {
            ObjectMapper om = new ObjectMapper();
            List<String> recipients = om.readValue(message.getRecipients(), om.getTypeFactory().constructCollectionType(List.class, String.class));

            recipients.forEach(recipient -> {
                int status = messageServiceCenter.submitMessage(message.getOriginatingAddress(), recipient, message.getContent());
                MessageLog messageLog = messageLogRepository.findByMessage(message);

                switch (status) {
                    case 0:
                        messageLog.setStatus("DONE");
                        messageLogRepository.save(messageLog);
                        return;
                    case 1:
                        messageLog.setStatus("FAILED");
                        messageLog.setFailedMessage("Number Unreachable");
                        break;
                    case 2:
                        messageLog.setStatus("FAILED");
                        messageLog.setFailedMessage("Network Error");
                        break;
                    case 3:
                        messageLog.setStatus("FAILED");
                        messageLog.setFailedMessage("Unknown Error");
                }

                messageLogRepository.save(messageLog);
                current.getAndIncrement();
            });

        }
    }
}
