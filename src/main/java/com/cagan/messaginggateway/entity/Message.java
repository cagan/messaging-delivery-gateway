package com.cagan.messaginggateway.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "messages")
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String content;

    @Column
    private String originatingAddress;

    @ManyToOne(targetEntity = Client.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column
    private String recipients;

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", originatingAddress='" + originatingAddress + '\'' +
                ", recipients='" + recipients + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) && Objects.equals(content, message.content) && Objects.equals(originatingAddress, message.originatingAddress) && Objects.equals(client, message.client) && Objects.equals(recipients, message.recipients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, originatingAddress, client, recipients);
    }
}
