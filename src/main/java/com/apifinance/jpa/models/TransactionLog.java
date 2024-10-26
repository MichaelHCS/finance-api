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
    private TransactionAction action;

    @Column(name = "timestamp", nullable = false)
    private ZonedDateTime timestamp;

    @Column(name = "details", nullable = true)
    private String details;

    public TransactionLog() {
    }

    public TransactionLog(Payment payment, TransactionAction action, ZonedDateTime timestamp, String details) {
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


    // Método para gerar uma mensagem de log detalhada
    public String generateLogMessage() {
        return String.format("Ação: %s, Detalhes: %s, Data e Hora: %s",
                action != null ? action : "Ação desconhecida",
                details != null ? details : "Detalhes não disponíveis",
                timestamp != null ? timestamp : "Data desconhecida");
    }

    @Override
    public String toString() {
        return "TransactionLog{"
                + "id=" + id
                + ", payment=" + (payment != null ? payment.getId() : "null") // Para evitar a impressão do objeto Payment completo
                + ", action=" + action
                + ", timestamp=" + timestamp
                + ", details='" + details + '\''
                + '}';
    }
}
