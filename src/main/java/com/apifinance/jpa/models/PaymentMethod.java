package com.apifinance.jpa.models;

import com.apifinance.jpa.enums.PaymentMethodDetails;
import com.apifinance.jpa.enums.PaymentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PaymentType type; // Tipo de pagamento (Cartão de crédito, Boleto, etc.)

    @Enumerated(EnumType.STRING)
    @Column(name = "details", nullable = false)
    private PaymentMethodDetails details; // Detalhes do pagamento (número do cartão, banco)

    // Construtores
    public PaymentMethod() {
        
    }

    public PaymentMethod(PaymentType type, PaymentMethodDetails details) {
        this.type = type;
        this.details = details;
    }

    // Getters e Setters
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
