package com.apifinance.jpa.models;

import java.time.ZonedDateTime;

import com.apifinance.jpa.enums.rabbitmqMessageStatus; // Corrigido para convenção de nomenclatura

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Classe que representa uma mensagem no RabbitMQ.
 */
@Entity
@Table(name = "rabbitmq_message")
public class RabbitMQMessage extends BaseEntity {

    @Column(name = "message_content", nullable = false)
    @NotNull
    private String messageContent; // Conteúdo da mensagem

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private rabbitmqMessageStatus status; // Status da mensagem

    @Column(name = "sent_at", nullable = false)
    @NotNull
    private ZonedDateTime sentAt; // Data e hora de envio da mensagem

    @Column(name = "processed_at") // Data e hora de processamento da mensagem
    private ZonedDateTime processedAt; // Pode ser nulo se não processado ainda

    // Construtor padrão
    public RabbitMQMessage() {}

    // Construtor com parâmetros
    public RabbitMQMessage(String messageContent, rabbitmqMessageStatus status, ZonedDateTime sentAt) {
        this.messageContent = messageContent;
        this.status = status;
        this.sentAt = sentAt;
    }

    // Getters e Setters
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

    // Override toString
    @Override
    public String toString() {
        return "RabbitMQMessage{" +
                "id=" + getId() + // Utiliza o getId() da BaseEntity
                ", messageContent='" + messageContent + '\'' +
                ", status=" + status +
                ", sentAt=" + sentAt +
                ", processedAt=" + processedAt +
                '}';
    }
}
