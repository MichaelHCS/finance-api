package com.apifinance.jpa.enums;

public enum rabbitmqMessageStatus {
    
    PENDING("Aguardando envio"),   // Mensagem foi criada e está aguardando envio/processamento
    SENT("Enviado"),               // Mensagem foi enviada
    PROCESSED("Processado"),       // Mensagem foi processada
    ERROR("Erro");                 // Ocorreu um erro ao processar a mensagem

    private final String description;

    rabbitmqMessageStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
