package com.apifinance.jpa.requests;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckResult;

import jakarta.validation.constraints.NotNull;

public class FraudCheckRequest {
    @NotNull
    private Long paymentId;
    
    @NotNull
    private FraudCheckResult fraudStatus; // Você pode definir o tipo conforme necessário

    @NotNull
    private String checkReason; // Mantenha como String

    // Getters e setters
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

    public String getCheckReason() {
        return checkReason;
    }

    public void setCheckReason(String checkReason) {
        this.checkReason = checkReason;
    }

    // Método para converter o String em Enum
    public FraudCheckReason getCheckReasonAsEnum() {
        return FraudCheckReason.valueOf(checkReason.toUpperCase()); // Converte String para Enum
    }
}
