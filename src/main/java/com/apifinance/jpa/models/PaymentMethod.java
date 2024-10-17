package com.apifinance.jpa.models;

import java.util.ArrayList;
import java.util.List;

import com.apifinance.jpa.enums.PaymentMethodType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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

    @Column(name = "details", nullable = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String details; // Detalhes do pagamento

    @OneToMany(mappedBy = "paymentMethod", fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>(); // Inicializa a lista de pagamentos

    // Construtor padrão
    public PaymentMethod() {
        // Necessário para o JPA
    }

    // Construtor com parâmetros
    public PaymentMethod(PaymentMethodType type, String details) {
        this.type = type;
        this.details = details;
    }

    // Getters e Setters
    public PaymentMethodType getType() {
        return type;
    }

    public void setType(PaymentMethodType type) {
        this.type = type;
    }


    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setPaymentMethod(type);// Define o PaymentMethod no Payment
    }

    // Override toString
    @Override
    public String toString() {
        return "PaymentMethod{" +
                "id=" + getId() + // Utiliza o getId() da BaseEntity
                ", type=" + type +
                ", details='" + details + '\'' +
                '}';
    }

    
}
