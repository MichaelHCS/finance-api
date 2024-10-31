package com.apifinance.jpa.models;

import java.time.ZonedDateTime;
import com.apifinance.jpa.enums.TransactionLogDetails;
import jakarta.persistence.*;

@Entity
@Table(name = "transaction_log")
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    }

    public TransactionLog(Payment payment, TransactionLogDetails action, ZonedDateTime timestamp, String details) {
        this.payment = payment;
        this.action = action;
        this.timestamp = timestamp;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    // Método para gerar uma mensagem de log detalhada
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
