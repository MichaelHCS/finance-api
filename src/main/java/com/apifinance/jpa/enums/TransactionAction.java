package com.apifinance.jpa.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionAction {
    PAYMENT_CREATED("Pagamento criado"),
    FRAUD_DETECTED("Fraude detectada");

    private final String description;

    TransactionAction(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static TransactionAction fromValue(String value) {
        for (TransactionAction action : TransactionAction.values()) {
            if (action.getDescription().equalsIgnoreCase(value)) {
                return action;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}

