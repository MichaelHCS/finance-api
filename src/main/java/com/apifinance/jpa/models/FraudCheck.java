package com.apifinance.jpa.models;

import java.time.ZonedDateTime;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckResult;

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
 * Classe que representa a verificação de fraude.
 */
@Entity
@Table(name = "fraud_check") // Tabela "fraud_check"
public class FraudCheck extends BaseEntity {

    @NotNull // Validação para garantir que o pagamento não seja nulo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false) // Referência à tabela Payment
    private Payment payment; // Pagamento associado a esta verificação de fraude

    @Enumerated(EnumType.STRING)
    @NotNull // Validação para garantir que o fraudStatus não seja nulo
    @Column(name = "fraud_status", nullable = false) // Atributo fraud_status
    private FraudCheckResult fraudStatus; // Resultado da verificação de fraude

    @Enumerated(EnumType.STRING)
    @Column(name = "check_reason") // Tornando-o opcional
    private FraudCheckReason checkReason; // Motivo do resultado da análise de fraude

    @NotNull // Validação para garantir que a data e hora não sejam nulas
    @Column(name = "fraud_checked_at", nullable = false) // Renomeado para fraud_checked_at
    private ZonedDateTime fraudCheckedAt; // Data e hora da verificação de fraude

    @Column(name = "rabbitmq_message_id") // Atributo rabbitmq_message_id
    private Long rabbitmqMessageId; // ID da mensagem RabbitMQ associada

    // Construtor padrão
    public FraudCheck() {}

    // Construtor com parâmetros
    public FraudCheck(Payment payment, FraudCheckResult fraudStatus, FraudCheckReason checkReason, ZonedDateTime fraudCheckedAt, Long rabbitmqMessageId) {
        this.payment = payment;
        this.fraudStatus = fraudStatus;
        this.checkReason = checkReason;
        this.fraudCheckedAt = fraudCheckedAt;
        this.rabbitmqMessageId = rabbitmqMessageId;
    }

    // Getters e Setters
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public FraudCheckResult getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(FraudCheckResult fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public FraudCheckReason getCheckReason() {
        return checkReason;
    }

    public void setCheckReason(FraudCheckReason checkReason) {
        this.checkReason = checkReason;
    }

    public ZonedDateTime getFraudCheckedAt() {
        return fraudCheckedAt;
    }

    public void setFraudCheckedAt(ZonedDateTime fraudCheckedAt) {
        this.fraudCheckedAt = fraudCheckedAt;
    }

    public Long getRabbitmqMessageId() {
        return rabbitmqMessageId;
    }

    public void setRabbitmqMessageId(Long rabbitmqMessageId) {
        this.rabbitmqMessageId = rabbitmqMessageId;
    }

    @Override
    public String toString() {
        return "FraudCheck{" +
                "id=" + getId() +  
                ", paymentId=" + (payment != null ? payment.getId() : null) + 
                ", fraudStatus=" + fraudStatus +
                ", checkReason=" + checkReason +
                ", fraudCheckedAt=" + fraudCheckedAt +
                ", rabbitmqMessageId=" + rabbitmqMessageId +
                '}';
    }
}
