package com.apifinance.jpa.enums;

public enum PaymentMethodDetailsType {
    CREDIT_CARD("Número do Cartão de Crédito"),
    BOLETO("Número do Boleto"),
    BANK_TRANSFER("Detalhes da Transferência Bancária"),
    PAYPAL("Conta PayPal"),
    PIX("Chave PIX");

    private final String description;

    PaymentMethodDetailsType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
