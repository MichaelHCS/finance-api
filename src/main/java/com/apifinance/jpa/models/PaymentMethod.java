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

@Entity
@Table(name = "payment_method")
public class PaymentMethod extends BaseEntity { 

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @NotNull
    private PaymentMethodType type; 

    @Column(name = "details", nullable = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String details; 

    @OneToMany(mappedBy = "paymentMethod", fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>(); 

    
    public PaymentMethod() {
        
    }

    
    public PaymentMethod(PaymentMethodType type, String details) {
        this.type = type;
        this.details = details;
    }

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
        payment.setPaymentMethod(type);
    }

    @Override
    public String toString() {
        return "PaymentMethod{" +
                "id=" + getId() + 
                ",type=" + type +
                ",details='" + details + '\'' +
                '}';
    }

    
}
