package com.cagan.messaginggateway.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "message_log")
@Data
public class MessageLog {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "message_id")
    private Message message;

    @Column
    private String status;

    @ManyToOne(targetEntity = Client.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column
    private String failedMessage;
}
