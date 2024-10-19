package com.apifinance.jpa.enums;

public enum FraudCheckResult {
    PENDING("Pendente"),
    APPROVED("Aprovado"),
    REJECTED("Rejeitado");

    private final String description; 

    FraudCheckResult(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
