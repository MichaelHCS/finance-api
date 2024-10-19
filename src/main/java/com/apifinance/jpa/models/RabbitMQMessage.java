package com.apifinance.jpa.models;

import java.time.ZonedDateTime;
import com.apifinance.jpa.enums.rabbitmqMessageStatus; 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "rabbitmq_message")
public class RabbitMQMessage extends BaseEntity {

    @Column(name = "message_content", nullable = false)
    @NotNull
    private String messageContent; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private rabbitmqMessageStatus status; 

    @Column(name = "sent_at", nullable = false)
    @NotNull
    private ZonedDateTime sentAt; 

    @Column(name = "processed_at") 
    private ZonedDateTime processedAt; 

    public RabbitMQMessage() {}

    public RabbitMQMessage(String messageContent, rabbitmqMessageStatus status, ZonedDateTime sentAt) {
        this.messageContent = messageContent;
        this.status = status;
        this.sentAt = sentAt;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public rabbitmqMessageStatus getStatus() {
        return status;
    }

    public void setStatus(rabbitmqMessageStatus status) {
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
        return "RabbitMQMessage{" +
                "id=" + getId() + 
                ", messageContent='" + messageContent + '\'' +
                ", status=" + status +
                ", sentAt=" + sentAt +
                ", processedAt=" + processedAt +
                '}';
    }
}
