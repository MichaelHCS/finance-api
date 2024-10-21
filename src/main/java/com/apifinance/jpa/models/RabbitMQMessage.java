package com.apifinance.jpa.models;

import java.time.ZonedDateTime;
import java.util.List;

import com.apifinance.jpa.enums.RabbitMqMessageStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "rabbitmq_message")
public class RabbitMqMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Gera o ID automaticamente
    private Long id; // ID da mensagem

    @Column(name = "message_content", nullable = false)
    @NotBlank // Garantir que o conteúdo da mensagem não esteja em branco
    private String messageContent; // Conteúdo da mensagem (JSON ou texto)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotBlank // Garantir que o status não esteja em branco
    private RabbitMqMessageStatus status; // Status da mensagem (enviado, processado, erro)

    @Column(name = "sent_at", nullable = false)
    @NotNull // Garantir que a data de envio não seja nula
    private ZonedDateTime sentAt; // Data e hora de envio da mensagem

    @Column(name = "processed_at") // Data e hora do processamento da mensagem
    private ZonedDateTime processedAt; // Inicialmente pode ser null

    @Transient
    @OneToMany(mappedBy = "rabbitMqMessage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;

    // Construtor padrão
    public RabbitMqMessage() {
        this.sentAt = ZonedDateTime.now(); // Inicializa sentAt com a data e hora atual
        this.processedAt = null; // Inicializa processedAt como null
    }

    // Construtor com parâmetros
    public RabbitMqMessage(String messageContent, RabbitMqMessageStatus status) {
        this.messageContent = messageContent;
        this.status = status;
        this.sentAt = ZonedDateTime.now(); // Define a data de envio
        this.processedAt = null; // Inicializa processedAt como null
    }

    // Getters e Setters
    public Long getId() {
        return id; // Getter para o ID
    }

    public void setId(Long id) {
        this.id = id; // Setter para o ID
    }

    public String getMessageContent() {
        return messageContent; // Getter para messageContent
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent; // Setter para messageContent
    }

    public RabbitMqMessageStatus getStatus() {
        return status; // Getter para status
    }

    public void setStatus(RabbitMqMessageStatus status) {
        this.status = status; // Setter para status
    }

    public ZonedDateTime getSentAt() {
        return sentAt; // Getter para sentAt
    }

    public void setSentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt; // Setter para sentAt
    }

    public ZonedDateTime getProcessedAt() {
        return processedAt; // Getter para processedAt
    }

    public void setProcessedAt(ZonedDateTime processedAt) {
        this.processedAt = processedAt; // Setter para processedAt
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    @Override
    public String toString() {
        return "RabbitMqMessage{"
                + "id=" + id // Inclui o ID na representação de string
                + ", messageContent='" + messageContent + '\'' // Inclui o conteúdo da mensagem
                + ", status='" + status.getDescription() + '\'' // Inclui o status
                + ", sentAt=" + sentAt // Inclui a data de envio
                + ", processedAt=" + processedAt // Inclui a data de processamento
                + '}';
    }

}
