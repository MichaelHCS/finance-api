package com.apifinance.jpa.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentType {
    CREDIT_CARD("Cartão de Crédito"),
    DEBIT_CARD("Cartão de Débito"),
    BOLETO("Boleto"),
    PIX("PIX");

    private final String description; 

    PaymentType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static PaymentType fromString(String value) {
        for (PaymentType paymentType : PaymentType.values()) {
            if (paymentType.name().equalsIgnoreCase(value)) {
                return paymentType;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentType: " + value);
    }

    

}
