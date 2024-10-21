package com.apifinance.jpa.models;

import java.time.ZonedDateTime;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fraud_check")
public class FraudCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "fraud_status", nullable = false)
    private FraudCheckStatus fraudStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_reason")
    private FraudCheckReason checkReason;

    @Column(name = "checked_at", nullable = false)
    private final ZonedDateTime checkedAt;

    @Column(name = "rabbitmq_message_id")
    private Long rabbitmqMessageId;

    public FraudCheck() {
        this.checkedAt = ZonedDateTime.now();
    }

    public FraudCheck(Long paymentId, FraudCheckStatus fraudStatus, FraudCheckReason checkReason, Long rabbitmqMessageId) {
        this.paymentId = paymentId;
        this.fraudStatus = fraudStatus;
        this.checkReason = checkReason;
        this.rabbitmqMessageId = rabbitmqMessageId;
        this.checkedAt = ZonedDateTime.now();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPaymentId() {
        return paymentId; // Getter para paymentId
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId; // Setter para paymentId
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

    public Long getRabbitmqMessageId() {
        return rabbitmqMessageId;
    }

    public void setRabbitmqMessageId(Long rabbitmqMessageId) {
        this.rabbitmqMessageId = rabbitmqMessageId;
    }

    @Override
    public String toString() {
        return "FraudCheck{"
                + "id=" + id
                + ", paymentId=" + paymentId
                + ", fraudStatus='" + fraudStatus + '\''
                + ", checkReason='" + checkReason + '\''
                + ", checkedAt=" + checkedAt
                + ", rabbitmqMessageId=" + rabbitmqMessageId
                + '}';
    }
}
