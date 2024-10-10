package com.apifinance.jpa.models;

import java.time.LocalDateTime;

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

    @Column(name = "payment_id", nullable = false)
    private Long paymentId; 

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private TransactionAction action; 

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp; 

    @Column(name = "details", columnDefinition = "TEXT")
    private String details; 

    // Relacionamento ManyToOne com FraudCheck
    @ManyToOne(fetch = FetchType.LAZY) // tipo de carregamento
    @JoinColumn(name = "fraud_check_id", nullable = false) // Coluna que referencia FraudCheck
    private FraudCheck fraudCheck; // Relacionamento ManyToOne com FraudCheck

    public TransactionLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public TransactionAction getAction() {
        return action;
    }

    public void setAction(TransactionAction action) {
        this.action = action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public FraudCheck getFraudCheck() {
        return fraudCheck;
    }

    public void setFraudCheck(FraudCheck fraudCheck) {
        this.fraudCheck = fraudCheck;
    }
}
