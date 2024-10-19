package com.apifinance.jpa.enums;

public enum PaymentMethodType {
    CREDIT_CARD("Cartão de Crédito"),
    DEBIT_CARD("Cartão de Débito"),
    BOLETO("Boleto"),
    PIX("PIX");

    private final String description; // Descrição do método de pagamento
   // private final String code; // Código do método de pagamento

    // Construtor da enumeração
    PaymentMethodType(String description) {
        this.description = description;
        //this.code = code;
    }

    // Getters
    public String getDescription() {
        return description;
    }

    //public String getCode() {
       // return code;
    //}

    // Método para buscar o enum a partir do código
    //public static PaymentMethodType fromCode(String code) {
       // for (PaymentMethodType method : PaymentMethodType.values()) {
         //   if (method.getCode().equals(code)) {
          //      return method;
          //  }
       // }
       // throw new IllegalArgumentException("Método de pagamento não encontrado: " + code);
   // }
}
