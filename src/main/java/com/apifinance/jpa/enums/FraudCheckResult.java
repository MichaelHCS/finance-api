package com.apifinance.jpa.enums;

public enum FraudCheckResult {
    PENDING("Pendente"),
    APPROVED("Aprovado"),
    REJECTED("Rejeitado");

    private final String description; // Detalhe sobre o resultado da verificação de fraude

    FraudCheckResult(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
