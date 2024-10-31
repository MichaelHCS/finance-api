package com.apifinance.jpa.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FraudCheckStatus {
    APPROVED("Aprovado"),
    REJECTED("Rejeitado");

    private final String description;

    FraudCheckStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

     @JsonCreator
    public static FraudCheckStatus fromValue(String value) {
        for (FraudCheckStatus status : FraudCheckStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status; // Retorna o enum correspondente ao valor string
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value); // Lança exceção se o valor não for reconhecido
    }
     public PaymentStatus toPaymentStatus() {
        // Criar um mapa para associar FraudCheckStatus a PaymentStatus
        Map<FraudCheckStatus, PaymentStatus> statusMapping = new HashMap<>();
        statusMapping.put(APPROVED, PaymentStatus.APPROVED);
        statusMapping.put(REJECTED, PaymentStatus.REJECTED);

        // Retornar o status correspondente ou lançar uma exceção se não encontrado
        PaymentStatus paymentStatus = statusMapping.get(this);
        if (paymentStatus == null) {
            throw new IllegalArgumentException("Status de pagamento inválido.");
        }
        return paymentStatus;
    }
}