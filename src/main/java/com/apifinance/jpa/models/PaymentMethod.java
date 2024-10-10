package com.apifinance.jpa.models;

import com.apifinance.jpa.enums.PaymentMethodDetailsType;
import com.apifinance.jpa.enums.PaymentMethodType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_method")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PaymentMethodType type; 

    @Enumerated(EnumType.STRING)
    @Column(name = "details_type", nullable = false)
    private PaymentMethodDetailsType detailsType; 

    @Column(name = "details", nullable = false)
    private String details; 

    // Relacionamento ManyToOne com Payment 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment; 

    
    public PaymentMethod() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
