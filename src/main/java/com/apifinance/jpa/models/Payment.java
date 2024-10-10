package com.apifinance.jpa.models;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;

import com.apifinance.jpa.enums.PaymentMethodType;
import com.apifinance.jpa.enums.PaymentStatus;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @ManyToOne 
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer; 

    @Enumerated(EnumType.STRING) 
    @Column(name = "payment_method", nullable = false)
    private PaymentMethodType paymentMethod; 

    @Column(nullable = false)
    private Double amount; 

    @Column(nullable = false)
    private String currency; 

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; 

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; 
    @Column(name = "fraud_check_id") 
    private Long fraudCheckId; 

    @Enumerated(EnumType.STRING) 
    @Column(name = "status", nullable = false)
    private PaymentStatus status; 

    public Payment() {
        
        this.createdAt = LocalDateTime.now(); 
        this.updatedAt = LocalDateTime.now(); 
        this.status = PaymentStatus.PENDING; 
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PaymentMethodType getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodType paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getFraudCheckId() {
        return fraudCheckId;
    }

    public void setFraudCheckId(Long fraudCheckId) {
        this.fraudCheckId = fraudCheckId;
    }

}
