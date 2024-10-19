package com.apifinance.jpa.enums;

public enum rabbitmqMessageStatus {
    
    PENDING("Aguardando envio"),   
    SENT("Enviado"),               
    PROCESSED("Processado"),       
    ERROR("Erro");                 

    private final String description;

    rabbitmqMessageStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
