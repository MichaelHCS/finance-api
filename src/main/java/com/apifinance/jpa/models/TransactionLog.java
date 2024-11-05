package com.apifinance.jpa.models;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.apifinance.jpa.enums.TransactionLogDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transaction_log")
public class TransactionLog {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private TransactionLogDetails action;

    @Column(name = "timestamp", nullable = false)
    private ZonedDateTime timestamp;

    @Column(name = "details", nullable = true)
    private String details;

    public TransactionLog() {
        this.id = UUID.randomUUID();
        this.timestamp = ZonedDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public TransactionLogDetails getAction() {
        return action;
    }

    public void setAction(TransactionLogDetails action) {
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

    public String generateLogMessage() {
        return String.format("Ação: %s, Detalhes: %s, Data e Hora: %s",
                action != null ? action : "Ação desconhecida",
                details != null ? details : "Detalhes não disponíveis",
                timestamp != null ? timestamp : "Data desconhecida");
    }

    @Override
    public String toString() {
        return String.format("TransactionLog{id=%d, payment=%s, action=%s, timestamp=%s, details='%s'}",
                id, payment != null ? payment.getId() : "null", action, timestamp, details);
    }

}
