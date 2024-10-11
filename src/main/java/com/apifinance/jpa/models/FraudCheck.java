package com.apifinance.jpa.models;

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

@Entity
@Table(name = "fraud_check") // Tabela "fraud_check"
public class FraudCheck extends BaseEntity {

    @NotNull // Validação para garantir que o paymentId não seja nulo
    @Column(name = "payment_id", nullable = false)
    private Long paymentId; // ID do pagamento associado

    @Enumerated(EnumType.STRING)
    @NotNull // Validação para garantir que o fraudStatus não seja nulo
    @Column(name = "fraud_status", nullable = false)
    private FraudCheckResult fraudStatus; // Status da verificação de fraude

    @Enumerated(EnumType.STRING)
    @NotNull // Validação para garantir que o checkReason não seja nulo
    @Column(name = "check_reason", nullable = false)
    private FraudCheckReason checkReason; // Razão da verificação de fraude

    @Column(name = "rabbitmq_message_id")
    private Long rabbitmqMessageId; // ID da mensagem RabbitMQ associada

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Payment payment; // Pagamento associado a esta verificação de fraude

    // Construtor padrão
    public FraudCheck() {
        // Construtor padrão
    }

    // Construtor com parâmetros
    public FraudCheck(Long paymentId, FraudCheckResult fraudStatus, FraudCheckReason checkReason, Long rabbitmqMessageId) {
        this.paymentId = paymentId;
        this.fraudStatus = fraudStatus;
        this.checkReason = checkReason;
        this.rabbitmqMessageId = rabbitmqMessageId;
    }

    // Getters e Setters
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
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

    public Long getRabbitmqMessageId() {
        return rabbitmqMessageId;
    }

    public void setRabbitmqMessageId(Long rabbitmqMessageId) {
        this.rabbitmqMessageId = rabbitmqMessageId;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    // Override toString
    @Override
    public String toString() {
        return "FraudCheck{" +
                "id=" + getId() +  // Utiliza o getId() da BaseEntity
                ", paymentId=" + paymentId +
                ", fraudStatus=" + fraudStatus +
                ", checkReason=" + checkReason +
                ", rabbitmqMessageId=" + rabbitmqMessageId +
                '}';
    }
}
