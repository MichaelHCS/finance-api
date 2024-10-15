package com.apifinance.jpa.controllers;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckResult;

import jakarta.validation.constraints.NotNull;

public class FraudCheckRequest {
    @NotNull
    private Long paymentId; // ID do pagamento analisado
    
    @NotNull
    private FraudCheckResult fraudStatus; // Status pode ser "APPROVED" ou "REJECTED"
    
    private FraudCheckReason checkReason; // Motivo da rejeição (opcional)

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
}
