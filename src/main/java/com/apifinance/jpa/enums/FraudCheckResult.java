package com.apifinance.jpa.enums;

public enum FraudCheckResult {
    PENDING("Pendente"),
    APPROVED("Aprovado"),
    REJECTED("Rejeitado");

    private final String detail; // Detalhe sobre o resultado da verificação de fraude

    FraudCheckResult(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }
}
