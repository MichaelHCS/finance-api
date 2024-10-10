package com.apifinance.jpa.models;

import java.time.LocalDateTime;

import com.apifinance.jpa.enums.MessageStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "rabbitmq_message")
public class RabbitMQMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column(name = "message_content", columnDefinition = "TEXT", nullable = false)
    private String messageContent; 

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MessageStatus status; 

    @Column(name = "sent_at", nullable = false, updatable = false)
    private LocalDateTime sentAt; 

    @Column(name = "processed_at")
    private LocalDateTime processedAt; 

    // Relacionamento ManyToOne com FraudCheck 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fraud_check_id", referencedColumnName = "id")
    private FraudCheck fraudCheck; // Relacionamento com FraudCheck

    // Construtor padrão
    public RabbitMQMessage() {
        this.sentAt = LocalDateTime.now(); // Inicializa a data de envio como a data atual
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

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public FraudCheck getFraudCheck() {
        return fraudCheck;
    }

    public void setFraudCheck(FraudCheck fraudCheck) {
        this.fraudCheck = fraudCheck;
    }
}
