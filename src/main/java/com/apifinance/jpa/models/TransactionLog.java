package com.apifinance.jpa.models;

import java.time.ZonedDateTime;

import com.apifinance.jpa.enums.TransactionAction;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "transaction_log")
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Gera o ID automaticamente
    private Long id; // ID do log

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false) // Ação realizada
    @NotBlank // Garantir que a ação não esteja em branco
    private TransactionAction action; // Ação realizada (ex: "Pagamento criado", "Fraude detectada")

    @Column(name = "timestamp", nullable = false) // Data e hora do log
    @NotNull // Garantir que o timestamp não seja nulo
    private ZonedDateTime timestamp; // Data e hora do log

    @Column(name = "details", nullable = false) // Detalhes adicionais sobre a ação
    private String details; // Detalhes adicionais (opcional)

    // Construtor padrão
    public TransactionLog() {
        this.timestamp = ZonedDateTime.now(); // Inicializa timestamp com a data e hora atual
    }

    // Construtor com parâmetros
    public TransactionLog(Payment payment, TransactionAction action, String details) {
        this.payment = payment;
        this.action = action;
        this.timestamp = ZonedDateTime.now(); // Define a data e hora do log
        this.details = details; // Define os detalhes adicionais
    }

    // Getters e Setters
    public Long getId() {
        return id; // Getter para o ID
    }

    public void setId(Long id) {
        this.id = id; // Setter para o ID
    }

    public Payment getPayment() {
        return payment; // Getter para paymentId
    }

    public void setPayment(Payment payment) {
        this.payment = payment; // Setter para paymentId
    }

    public TransactionAction getAction() {
        return action;
    }

    public void setAction(TransactionAction action) {
        this.action = action;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp; // Getter para timestamp
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp; // Setter para timestamp
    }

    public String getDetails() {
        return details; // Getter para details
    }

    public void setDetails(String details) {
        this.details = details; // Setter para details
    }

    @Override
    public String toString() {
        return "TransactionLog{"
                + "id=" + id // Inclui o ID na representação de string
                + ", payment=" + (payment != null ? payment.getId() : null)
                + ", action=" + action + '\'' // Inclui a ação realizada
                + ", timestamp=" + timestamp // Inclui a data e hora do log
                + ", details='" + details + '\'' // Inclui os detalhes adicionais
                + '}';
    }
}
