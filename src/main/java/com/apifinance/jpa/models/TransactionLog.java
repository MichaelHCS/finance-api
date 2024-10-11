package com.apifinance.jpa.models;

import com.apifinance.jpa.enums.TransactionAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Classe que representa um registro de transação, incluindo informações sobre
 * o pagamento, a ação realizada, e os detalhes da transação.
 */
@Entity
@Table(name = "transaction_log")
public class TransactionLog extends BaseEntity {

    @NotNull
    @Column(name = "payment_id", nullable = false)
    private Long paymentId; // ID do pagamento associado

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private TransactionAction action; // Ação da transação

    @Column(name = "details", columnDefinition = "TEXT")
    private String details; // Detalhes da transação

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fraud_check_id", nullable = false)
    private FraudCheck fraudCheck; // Verificação de fraude associada

    // Construtor padrão
    public TransactionLog() {
    }

    // Construtor com parâmetros
    public TransactionLog(Long paymentId, TransactionAction action, FraudCheck fraudCheck, String details) {
        this.paymentId = paymentId;
        this.action = action;
        this.fraudCheck = fraudCheck;
        this.details = details;
    }

    // Getters e Setters
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

    // Override toString
    @Override
    public String toString() {
        return "TransactionLog{" +
                "id=" + getId() + // Utiliza o getId() da BaseEntity
                ", paymentId=" + paymentId +
                ", action=" + action +
                ", details='" + details + '\'' +
                ", fraudCheck=" + fraudCheck +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
