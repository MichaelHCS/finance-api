package com.apifinance.jpa.models;

import java.time.ZonedDateTime;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckStatus;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fraud_check")
public class FraudCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment; // Supondo que a entidade Payment já esteja criada

    @Enumerated(EnumType.STRING)
    @Column(name = "fraud_status", nullable = false)
    private FraudCheckStatus fraudStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_reason", nullable = false)
    private FraudCheckReason checkReason;

    @Column(name = "checked_at", nullable = false)
    private ZonedDateTime checkedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rabbitmq_message_id")
    private RabbitMqMessage rabbitMqMessage; // Supondo que tenha uma entidade RabbitmqMessage


    // Construtores, getters e setters
    public FraudCheck(Payment payment, FraudCheckStatus fraudStatus, FraudCheckReason checkReason, ZonedDateTime checkedAt, RabbitMqMessage rabbitMqMessage) {
        this.payment = payment;
        this.fraudStatus = fraudStatus;
        this.checkReason = checkReason;
        this.checkedAt = checkedAt != null ? checkedAt : ZonedDateTime.now();
        this.rabbitMqMessage = rabbitMqMessage;

    }

    // Getters e Setters
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

    public FraudCheckStatus getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(FraudCheckStatus fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public FraudCheckReason getCheckReason() {
        return checkReason;
    }

    public void setCheckReason(FraudCheckReason checkReason) {
        this.checkReason = checkReason;
    }

    public ZonedDateTime getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(ZonedDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }

    public RabbitMqMessage getRabbitMqMessage() {
        return rabbitMqMessage;
    }

    public void setRabbitMqMessage(RabbitMqMessage rabbitMqMessage) {
        this.rabbitMqMessage = rabbitMqMessage;
    }

    @Override
    public String toString() {
        return "FraudCheck{"
                + "id=" + id
                + ", payment=" + (payment != null ? payment.getId() : "null") // Evita carregar toda a entidade de forma preguiçosa
                + ", fraudStatus=" + fraudStatus
                + ", checkReason=" + checkReason
                + ", checkedAt=" + checkedAt
                + ", rabbitMqMessage=" + (rabbitMqMessage != null ? rabbitMqMessage.getId() : "null") // Evita carregar toda a entidade de forma preguiçosa
                + '}';
    }

}
