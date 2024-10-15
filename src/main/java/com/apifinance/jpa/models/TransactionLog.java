package com.apifinance.jpa.models;

import java.time.ZonedDateTime;

import com.apifinance.jpa.enums.TransactionAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Classe que representa um log de transação.
 */
@Entity
@Table(name = "transaction_log")
public class TransactionLog extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false) // Chave estrangeira para Payment
    @NotNull
    private Payment payment; // Referência ao pagamento associado

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    @NotNull
    private TransactionAction action; // Ação realizada na transação

    @Column(name = "timestamp", nullable = false)
    @NotNull
    private ZonedDateTime timestamp; // Data e hora da ação

    @Column(name = "details") // Detalhes adicionais sobre a ação
    private String details;

    // Construtor padrão
    public TransactionLog() {}

    // Construtor com parâmetros
    public TransactionLog(Payment payment, TransactionAction action, String details) {
        this.payment = payment;
        this.action = action;
        this.timestamp = ZonedDateTime.now(); // Define timestamp na criação
        this.details = details;
    }

    // Getters e Setters
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public TransactionAction getAction() {
        return action;
    }

    public void setAction(TransactionAction action) {
        this.action = action;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    // Override toString
    @Override
    public String toString() {
        return "TransactionLog{" +
                "id=" + getId() + // Utiliza o getId() da BaseEntity
                ", payment_id=" + (payment != null ? payment.getId() : null) + // ID do pagamento associado
                ", action=" + action +
                ", timestamp=" + timestamp +
                ", details='" + details + '\'' + // Detalhes adicionais
                '}';
    }
}
