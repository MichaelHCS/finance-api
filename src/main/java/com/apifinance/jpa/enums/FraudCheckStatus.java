package com.apifinance.jpa.enums;

public enum FraudCheckStatus {
    APPROVED("Aprovado"),
    REJECTED("Rejeitado");

    private final String description;

    FraudCheckStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
