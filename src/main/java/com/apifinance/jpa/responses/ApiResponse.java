package com.apifinance.jpa.responses;

import java.util.UUID;

public class ApiResponse {
    private String message;
    private UUID paymentId;

    public ApiResponse(String message) {
        this.message = message;
    }

    public ApiResponse(String message, UUID paymentId) {
        this.message = message;
        this.paymentId = paymentId;
    }

    public String getMessage() {
        return message;
    }

    public UUID getPaymentId() {
        return paymentId;
    }
}
