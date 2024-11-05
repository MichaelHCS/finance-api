package com.apifinance.jpa.models;

import java.util.UUID;


import com.apifinance.jpa.enums.PaymentType;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_method")
public class PaymentMethod {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "type", nullable = false)
    private PaymentType type; 

    @Column(name = "details", nullable = false)
    private String details; 

    public PaymentMethod() {
        
    }

    public PaymentMethod(PaymentType type, String details) {
        this.type = type;
        this.details = details;
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return String.format("PaymentMethod{id=%d, type=%s, details=%s}", id, type, details);
    }

}
