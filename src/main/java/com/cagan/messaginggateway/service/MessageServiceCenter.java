package com.cagan.messaginggateway.service;

/**
 * Message Service Center interface which simulates a GSM operator.
 */
public class MessageServiceCenter {
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
    }
}
