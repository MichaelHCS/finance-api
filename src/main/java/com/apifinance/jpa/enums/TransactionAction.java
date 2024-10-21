package com.apifinance.jpa.enums;

public enum TransactionAction {
    PAYMENT_CREATED("Pagamento criado"),
    FRAUD_DETECTED("Fraude detectada");
    //PAYMENT_APPROVED("Pagamento aprovado"),   
    //PAYMENT_REJECTED("Pagamento rejeitado");  

    private final String description;

    TransactionAction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
