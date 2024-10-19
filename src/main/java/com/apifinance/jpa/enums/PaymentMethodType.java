package com.apifinance.jpa.enums;

public enum PaymentMethodType {
    CREDIT_CARD("Cartão de Crédito"),
    DEBIT_CARD("Cartão de Débito"),
    BOLETO("Boleto"),
    PIX("PIX");

    private final String description; // Descrição do método de pagamento
   
    PaymentMethodType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }

   
}
