package com.apifinance.jpa.dtos;

import java.util.UUID;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckStatus;

public class FraudCheckRequest {

    private UUID paymentId;
    private UUID rabbitMqMessageId;
    private FraudCheckStatus fraudStatus;
    private FraudCheckReason fraudReason;

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
}
