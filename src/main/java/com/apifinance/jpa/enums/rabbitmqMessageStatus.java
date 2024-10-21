package com.apifinance.jpa.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RabbitMqMessageStatus {

    SENT("Enviado"),
    PROCESSED("Processado"),
    ERROR("Erro");

    private final String description;

    RabbitMqMessageStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static RabbitMqMessageStatus fromValue(String value) {
        for (RabbitMqMessageStatus status : RabbitMqMessageStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status; // Retorna o enum correspondente ao valor string
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value); // Lança exceção se o valor não for reconhecido
    }
}
