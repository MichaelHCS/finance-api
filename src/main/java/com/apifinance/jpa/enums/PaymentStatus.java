package com.apifinance.jpa.enums;

public enum PaymentStatus {
    PENDING("Aguardando análise de fraude"),
    APPROVED("Pagamento aprovado"),
    REJECTED("Pagamento rejeitado");

    private final String detail; // Detalhe sobre o status

    // Construtor da enumeração
    PaymentStatus(String detail) {
        this.detail = detail;
    }

    // Método para obter o detalhe do status
    public String getDetail() {
        return detail;
    }
}
