package com.apifinance.jpa.models;

import com.apifinance.jpa.enums.PaymentMethodDetails;
import com.apifinance.jpa.enums.PaymentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_method")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único do método de pagamento

    @Column(name = "type", nullable = false)
    private PaymentType type; 

    @Column(name = "details", nullable = false)
    private PaymentMethodDetails details; 

    public PaymentMethod() {
        
    }

    public PaymentMethod(PaymentType type, PaymentMethodDetails details) {
        this.type = type;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public PaymentMethodDetails getDetails() {
        return details;
    }

    public void setDetails(PaymentMethodDetails details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "PaymentMethod{"
                + "id=" + id
                + ", type=" + type
                + ", details=" + details
                + '}';
    }

}
