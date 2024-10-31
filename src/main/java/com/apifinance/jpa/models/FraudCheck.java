package com.apifinance.jpa.models;

import java.time.ZonedDateTime;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "fraud_check")
public class FraudCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "fraud_status", nullable = false)
    private FraudCheckStatus fraudStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_reason", nullable = false)
    private FraudCheckReason fraudReason;

    @Column(name = "checked_at", nullable = false)
    private ZonedDateTime checkedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rabbitmq_message_id")
    private RabbitMqMessage rabbitMqMessage;

    public FraudCheck() {
        this.checkedAt = ZonedDateTime.now();
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

    public FraudCheckStatus getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(FraudCheckStatus fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public FraudCheckReason getFraudReason() {
        return fraudReason;
    }

    public void setFraudReason(FraudCheckReason fraudReason) {
        this.fraudReason = fraudReason;
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
        return String.format("FraudCheck{id=%d, payment=%s, fraudStatus=%s, fraudReason=%s, checkedAt=%s, rabbitMqMessage=%s}",
                id,
                (payment != null ? payment.getId() : "null"),
                fraudStatus,
                fraudReason,
                checkedAt,
                (rabbitMqMessage != null ? rabbitMqMessage.getId() : "null"));
    }

}
