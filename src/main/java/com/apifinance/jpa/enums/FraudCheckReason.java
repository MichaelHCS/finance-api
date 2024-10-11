package com.apifinance.jpa.enums;

public enum FraudCheckReason {
    HIGH_SCORE("Score alto"),
    SUSPICIOUS_LOCATION("Localização suspeita"),
    MISMATCHED_INFORMATION("Informações divergentes"),
    BLACKLISTED_CUSTOMER("Cliente em lista negra"),
    UNUSUAL_TRANSACTION_AMOUNT("Valor de transação incomum");

    private final String description;

    FraudCheckReason(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
