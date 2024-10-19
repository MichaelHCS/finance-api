package com.apifinance.jpa.enums;

public enum PaymentMethodDetailsType {
    CARD_NUMBER("Número do Cartão de Crédito"),
    BANK("Banco");

    private final String description;

    PaymentMethodDetailsType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
