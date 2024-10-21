package com.apifinance.jpa.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FraudCheckReason {
    NONE("Nenhum motivo"),
    HIGH_SCORE("Score alto"),
    SUSPICIOUS_LOCATION("Localização suspeita");
   
    private final String description;

    FraudCheckReason(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static FraudCheckReason fromValue(String value) {
        for (FraudCheckReason reason : FraudCheckReason.values()) {
            if (reason.name().equalsIgnoreCase(value)) {
                return reason; // Retorna o enum correspondente ao valor string
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value); // Lança exceção se o valor não for reconhecido
    }
}
