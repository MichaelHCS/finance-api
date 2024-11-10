package com.apifinance.jpa.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FraudCheckStatus {
    APPROVED("Aprovado"),
    REJECTED("Rejeitado");

    private final String description;

    FraudCheckStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

     @JsonCreator
    public static FraudCheckStatus fromValue(String value) {
        for (FraudCheckStatus status : FraudCheckStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status; 
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value); 
    }
     public PaymentStatus toPaymentStatus() {
        Map<FraudCheckStatus, PaymentStatus> statusMapping = new HashMap<>();
        statusMapping.put(APPROVED, PaymentStatus.APPROVED);
        statusMapping.put(REJECTED, PaymentStatus.REJECTED);

        PaymentStatus paymentStatus = statusMapping.get(this);
        if (paymentStatus == null) {
            throw new IllegalArgumentException("Status de pagamento inválido.");
        }
        return paymentStatus;
    }
}