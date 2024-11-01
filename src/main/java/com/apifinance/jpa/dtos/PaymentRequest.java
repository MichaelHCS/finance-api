package com.apifinance.jpa.dtos;

import com.apifinance.jpa.models.Payment;

public class PaymentRequest {
    private Payment payment; // Objeto Payment
    private FraudCheckRequest fraudCheckRequest; // Objeto FraudCheckRequest

    // Getters e Setters
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public FraudCheckRequest getFraudCheckRequest() {
        return fraudCheckRequest;
    }

    public void setFraudCheckRequest(FraudCheckRequest fraudCheckRequest) {
        this.fraudCheckRequest = fraudCheckRequest;
    }
}
