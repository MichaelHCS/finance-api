package com.apifinance.jpa.requests;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckResult;

import jakarta.validation.constraints.NotNull;

public class FraudCheckRequest {

    @NotNull
    private Long paymentId;

    @NotNull
    private FraudCheckResult fraudStatus;

    private FraudCheckReason checkReason; 

    private long fraudCheck;
    
    private Long rabbitmqMessage;

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

    public Long getRabbitmqMessage() {
        return rabbitmqMessage;
    }

    public void setRabbitmqMessage(Long rabbitmqMessage) {
        this.rabbitmqMessage = rabbitmqMessage;
    }

    public long getFraudCheck() {
        return fraudCheck;
    }

    public void setFraudCheck(long fraudCheck) {
        this.fraudCheck = fraudCheck;
    }
}
