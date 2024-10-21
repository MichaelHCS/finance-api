package com.apifinance.jpa.enums;

public enum FraudCheckReason {
    NONE("Nenhum motivo"),
    HIGH_SCORE("Score alto"),
    SUSPICIOUS_LOCATION("Localização suspeita");
   
    private final String description;

    FraudCheckReason(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
