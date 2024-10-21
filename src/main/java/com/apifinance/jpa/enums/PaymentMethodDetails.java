package com.apifinance.jpa.enums;

public enum PaymentMethodDetails {
    CARD_NUMBER("Número do Cartão de Crédito"),
    BANK("Banco");

    private final String description;

    PaymentMethodDetails(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
