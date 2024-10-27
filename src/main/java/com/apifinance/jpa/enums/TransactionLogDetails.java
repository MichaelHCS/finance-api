package com.apifinance.jpa.enums;

import java.math.BigDecimal;
import com.apifinance.jpa.models.Payment;

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
                String paymentTypeDescription = payment.getPaymentType() != null ? 
                    payment.getPaymentType().getDescription() : "Método de pagamento desconhecido";

                return String.format(
                    "Registro de transação: montante de %.2f %s processado por meio do método '%s'.",
                    paymentAmount,
                    paymentCurrency,
                    paymentTypeDescription
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
                String reasonDescription = (reason != null && reason.getDescription() != null)
                    ? reason.getDescription()
                    : "Nenhum motivo fornecido";

                BigDecimal paymentAmount = payment.getAmount() != null ? payment.getAmount() : BigDecimal.ZERO;
                String paymentCurrency = payment.getCurrency() != null ? payment.getCurrency() : "Moeda desconhecida";
                
                return String.format(
                    "Registro de fraude detectada: montante de %.2f %s, motivo: %s.",
                    paymentAmount,
                    paymentCurrency,
                    reasonDescription
                );
            } catch (Exception e) {
                return "Erro ao formatar detalhes de fraude: " + e.getMessage();
            }
        }
    };

    public abstract String getDetails(Payment payment, FraudCheckReason reason);
}
