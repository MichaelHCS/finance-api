package com.apifinance.jpa.enums;

public enum TransactionAction {
    PAYMENT_CREATED("Pagamento criado"),      // Ação de criação de pagamento
    FRAUD_DETECTED("Fraude detectada"),       // Ação de detecção de fraude
    PAYMENT_APPROVED("Pagamento aprovado"),   // Ação de aprovação do pagamento
    PAYMENT_REJECTED("Pagamento rejeitado");  // Ação de rejeição do pagamento

    private final String description;

    TransactionAction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
