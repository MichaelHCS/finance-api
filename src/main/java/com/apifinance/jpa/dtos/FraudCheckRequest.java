package com.apifinance.jpa.dtos;

import com.apifinance.jpa.enums.FraudCheckStatus;
import com.apifinance.jpa.enums.FraudCheckReason;

public class FraudCheckRequest {

    private Long paymentId;
    private Long rabbitMqMessageId;
    private FraudCheckStatus fraudStatus;
    private FraudCheckReason fraudReason;

    // Getters e setters
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getRabbitMqMessageId() {
        return rabbitMqMessageId;
    }

    public void setRabbitMqMessageId(Long rabbitMqMessageId) {
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
