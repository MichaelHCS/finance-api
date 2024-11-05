package com.apifinance.jpa.dtos;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckStatus;

public class FraudCheckResponse {

    private UUID id;
    private UUID paymentId;
    private UUID rabbitMqMessageId;
    private FraudCheckStatus fraudStatus;
    private FraudCheckReason fraudReason;
    private ZonedDateTime checkedAt;

    public FraudCheckResponse(UUID id, UUID paymentId, UUID rabbitMqMessageId,
                              FraudCheckStatus fraudStatus, FraudCheckReason fraudReason, ZonedDateTime checkedAt) {
        this.id = id;
        this.paymentId = paymentId;
        this.rabbitMqMessageId = rabbitMqMessageId;
        this.fraudStatus = fraudStatus;
        this.fraudReason = fraudReason;
        this.checkedAt = checkedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public UUID getRabbitMqMessageId() {
        return rabbitMqMessageId;
    }

    public void setRabbitMqMessageId(UUID rabbitMqMessageId) {
        this.rabbitMqMessageId = rabbitMqMessageId;
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
    

















}