package com.apifinance.jpa.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentMethodDetails {
    CARD_NUMBER("Número do Cartão de Crédito"),
    BANK("Banco");

    private final String description;

    PaymentMethodDetails(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static PaymentMethodDetails fromValue(String value) {
        for (PaymentMethodDetails detail : PaymentMethodDetails.values()) {
            if (detail.name().equalsIgnoreCase(value)) {
                return detail; // Retorna o enum correspondente ao valor string
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value); // Lança exceção se o valor não for reconhecido
    }
}
