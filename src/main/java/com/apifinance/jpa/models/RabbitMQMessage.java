package com.apifinance.jpa.models;

import java.time.ZonedDateTime;

import com.apifinance.jpa.enums.RabbitMqMessageStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "rabbitmq_message")
public class RabbitMqMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_content", nullable = false)
    private String messageContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RabbitMqMessageStatus status;

    @Column(name = "sent_at", nullable = false)
    private ZonedDateTime sentAt;

    @Column(name = "processed_at")
    private ZonedDateTime processedAt;

    public RabbitMqMessage() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public RabbitMqMessageStatus getStatus() {
        return status;
    }

    public void setStatus(RabbitMqMessageStatus status) {
        this.status = status;
    }

    public ZonedDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public ZonedDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(ZonedDateTime processedAt) {
        this.processedAt = processedAt;
    }

    @Override
    public String toString() {
        return String.format("RabbitMqMessage{id=%d, messageContent='%s', status=%s, sentAt=%s, processedAt=%s}",
                id, messageContent, status, sentAt, processedAt);
    }


}
