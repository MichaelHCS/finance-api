package com.apifinance.jpa.enums;

public enum RabbitMqMessageStatus {

    SENT("Enviado"),
    PROCESSED("Processado"),
    ERROR("Erro");

    private final String description;

    RabbitMqMessageStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
