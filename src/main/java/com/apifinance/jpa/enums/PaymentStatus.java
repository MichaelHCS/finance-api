package com.apifinance.jpa.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentStatus {
    PENDING("Aguardando análise de fraude"),
    APPROVED("Pagamento aprovado"),
    REJECTED("Pagamento rejeitado");

    private final String description;// Detalhe sobre o status

    // Construtor da enumeração
    PaymentStatus(String description) {
        this.description = description;
    }

    // Método para obter o detalhe do status
    @JsonValue
    public String getDetail() {
        return description;
    }

    @JsonCreator
    public static PaymentStatus fromValue(String value) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status; // Retorna o enum correspondente ao valor string
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value); // Lança exceção se o valor não for reconhecido
    }
}
