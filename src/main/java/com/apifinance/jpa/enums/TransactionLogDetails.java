package com.apifinance.jpa.enums;

import com.apifinance.jpa.models.Payment;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public enum TransactionLogDetails {

    PAYMENT_CREATED {
        @Override
        public String getDetails(Payment payment, FraudCheckReason reason) {
            if (payment == null) {
                return "O objeto Payment é nulo";
            }
            try {
                BigDecimal paymentAmount = payment.getAmount() != null ? payment.getAmount() : BigDecimal.ZERO;
                String paymentCurrency = payment.getCurrency() != null ? payment.getCurrency() : "Moeda desconhecida";
                String paymentCreatedAt = payment.getCreatedAt() != null ? 
                    payment.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Data desconhecida";
                String paymentTypeDescription = payment.getPaymentType() != null ? 
                    payment.getPaymentType().getDescription() : "Tipo de pagamento desconhecido";

                return String.format(
                    "Registro de pagamento: Valor %.2f %s processado via %s na data %s.",
                    paymentAmount,
                    paymentCurrency,
                    paymentTypeDescription,
                    paymentCreatedAt
                );
            } catch (Exception e) {
                return "Erro ao formatar detalhes do pagamento: " + e.getMessage();
            }
        }
    },

    FRAUD_DETECTED {
        @Override
        public String getDetails(Payment payment, FraudCheckReason reason) {
            if (payment == null) {
                return "O objeto Payment é nulo";
            }
            try {
                // Obtenha a descrição do motivo
                String reasonDescription = (reason != null && reason.getDescription() != null)
                    ? reason.getDescription()
                    : "Nenhum motivo fornecido";
    
                // Obtenha os detalhes do pagamento
                BigDecimal paymentAmount = payment.getAmount() != null ? payment.getAmount() : BigDecimal.ZERO;
                String paymentCurrency = payment.getCurrency() != null ? payment.getCurrency() : "Moeda desconhecida";
                String paymentCreatedAt = payment.getCreatedAt() != null ? payment.getCreatedAt().toLocalDate().toString() : "Data desconhecida"; // Apenas a data
                
                return String.format(
                    "Registro de Fraude Detectada: Montante de %.2f %s, Causa: %s, Data da Transação: %s.",
                    paymentAmount,
                    paymentCurrency,
                    reasonDescription,
                    paymentCreatedAt
                );
            } catch (Exception e) {
                return "Erro ao formatar detalhes de fraude: " + e.getMessage();
            }
        }
    };
    

    public abstract String getDetails(Payment payment, FraudCheckReason reason);
}
