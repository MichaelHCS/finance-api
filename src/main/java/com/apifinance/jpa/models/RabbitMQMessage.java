package com.apifinance.jpa.models;

import java.time.ZonedDateTime;

import com.apifinance.jpa.enums.RabbitMqMessageStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rabbitmq_message")
public class RabbitMqMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Identificador único da mensagem

    @Column(name = "message_content", nullable = false)
    private String messageContent;  // Conteúdo da mensagem (JSON ou texto)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RabbitMqMessageStatus status;  // Status da mensagem (enviado, processado, erro)

    @Column(name = "sent_at", nullable = false)
    private ZonedDateTime sentAt;  // Data e hora de envio da mensagem

    @Column(name = "processed_at")
    private ZonedDateTime processedAt;  // Data e hora do processamento da mensagem (pode ser nula)
    
    public RabbitMqMessage(){
        
    }
    // Getters e Setters

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
}
