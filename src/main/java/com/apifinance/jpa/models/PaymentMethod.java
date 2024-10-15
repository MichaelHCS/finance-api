package com.apifinance.jpa.models;

import com.apifinance.jpa.enums.PaymentMethodDetailsType;
import com.apifinance.jpa.enums.PaymentMethodType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Classe que representa um método de pagamento associado a um pagamento.
 */
@Entity
@Table(name = "payment_method")
public class PaymentMethod extends BaseEntity { // Extende BaseEntity

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @NotNull
    private PaymentMethodType type; // Tipo de pagamento

    @Enumerated(EnumType.STRING)
    @Column(name = "details_type", nullable = false)
    @NotNull
    private PaymentMethodDetailsType detailsType; // Tipo de detalhes do pagamento

    @Column(name = "details", nullable = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String details; // Detalhes do pagamento

    // Relacionamento ManyToOne com Payment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", nullable = false)
    private Payment payment; // Pagamento associado

    // Construtor padrão
    public PaymentMethod() {
        // Necessário para o JPA
    }

    // Construtor com parâmetros
    public PaymentMethod(PaymentMethodType type, PaymentMethodDetailsType detailsType, String details, Payment payment) {
        this.type = type;
        this.detailsType = detailsType;
        this.details = details;
        this.payment = payment;
    }

    // Getters e Setters
    public PaymentMethodType getType() {
        return type;
    }

    public void setType(PaymentMethodType type) {
        this.type = type;
    }

    public PaymentMethodDetailsType getDetailsType() {
        return detailsType;
    }

    public void setDetailsType(PaymentMethodDetailsType detailsType) {
        this.detailsType = detailsType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    // Override toString
    @Override
    public String toString() {
        return "PaymentMethod{" +
                "id=" + getId() + // Utiliza o getId() da BaseEntity
                ", type=" + type +
                ", detailsType=" + detailsType +
                ", details='" + details + '\'' +
                ", paymentId=" + (payment != null ? payment.getId() : null) + // Referência ao ID do pagamento
                '}';
    }
}
