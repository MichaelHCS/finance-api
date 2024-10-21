package com.apifinance.jpa.enums;

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
                return status; // Retorna o enum correspondente ao valor string
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value); // Lança exceção se o valor não for reconhecido
    }
}
