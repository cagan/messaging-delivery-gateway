package com.cagan.messaginggateway.service;

import com.cagan.messaginggateway.MessaginggatewayApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
/**
 *
 * Message Service Center interface which simulates a GSM operator.
 */
public class MessageServiceCenter {
    private static final Logger log = LoggerFactory.getLogger(MessaginggatewayApplication.class);
    private static int counter = 0;
    /**
     * Sends a message submission request to the GSM operator's network. Later GSM operator
     * will forward the request to the recipient and returns the result code of the operation.
     *
     * Available result codes: * 0: Success
     * 1: Number unreachable
     * 2: Network error
     * 3: Unknown error
     *
     * @param senderAddress
    recipient's cell phone.
     * @param destinationNumber
     * @param messageBody recipient.
    The number which will be shown on
    Recipient's cell phone number.
    Message text to be sent to the
     * @return
     */
    public int submitMessage(String senderAddress, String destinationNumber, String messageBody) {
        return (int) (Math.random() * 4);
//        return 4;
    }
}
