package com.apifinance.jpa.models;

import java.time.ZonedDateTime;

import com.apifinance.jpa.enums.MessageStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Classe que representa uma mensagem RabbitMQ com informações sobre seu conteúdo,
 * status e timestamps.
 */
@Entity
@Table(name = "rabbitmq_message")
public class RabbitMQMessage extends BaseEntity { // Extende BaseEntity

    @Column(name = "message_content", columnDefinition = "TEXT", nullable = false)
    @NotNull(message = "Message content cannot be null")
    private String messageContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MessageStatus status;

    @Column(name = "sent_at", nullable = false, updatable = false)
    private ZonedDateTime sentAt;

    @Column(name = "processed_at")
    private ZonedDateTime processedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fraud_check_id", referencedColumnName = "id")
    private FraudCheck fraudCheck;

    // Construtor padrão
    public RabbitMQMessage() {
        // O sentAt será definido no método @PrePersist
    }

    // Construtor com parâmetros
    public RabbitMQMessage(String messageContent, MessageStatus status) {
        this.messageContent = messageContent;
        this.status = status;
    }

    @PrePersist
    @Override
    protected void onCreate() {
        this.sentAt = ZonedDateTime.now();
    }

    // Getters e Setters
    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public ZonedDateTime getSentAt() {
        return sentAt;
    }

    public ZonedDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(ZonedDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public FraudCheck getFraudCheck() {
        return fraudCheck;
    }

    public void setFraudCheck(FraudCheck fraudCheck) {
        this.fraudCheck = fraudCheck;
    }

    @Override
    public String toString() {
        return "RabbitMQMessage{" +
                "id=" + getId() +  // Utiliza o getId() da BaseEntity
                ", messageContent='" + messageContent + '\'' +
                ", status=" + status +
                ", sentAt=" + sentAt +
                ", processedAt=" + processedAt +
                ", fraudCheck=" + (fraudCheck != null ? fraudCheck.getId() : null) + // Mostra apenas o ID da fraudCheck para evitar recursão infinita
                '}';
    }
}
