package com.apifinance.jpa.enums;

public enum MessageStatus {
    SENT("Enviado"),         // Mensagem foi enviada
    PROCESSED("Processado"),  // Mensagem foi processada
    ERROR("Erro");            // Ocorreu um erro ao processar a mensagem

    private final String description;

    MessageStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
