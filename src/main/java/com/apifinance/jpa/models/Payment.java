package com.apifinance.jpa.models;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.enums.PaymentType;

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
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer; // Supondo que já tenha uma entidade Customer

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentType paymentMethod;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fraud_check_id")
    private FraudCheck fraudCheck; // Supondo que você tenha uma entidade FraudCheck

    // Construtores, getters e setters
    public Payment() {
        this.customer = customer;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.currency = currency;
        this.status = status != null ? status : PaymentStatus.PENDING; // Define status padrão
        this.createdAt = createdAt != null ? createdAt : ZonedDateTime.now();
        this.updatedAt = updatedAt;
        this.fraudCheck = fraudCheck;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PaymentType getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentType paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setPaymentStatus(PaymentStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public FraudCheck getFraudCheck() {
        return fraudCheck;
    }

    public void setFraudCheck(FraudCheck fraudCheck) {
        this.fraudCheck = fraudCheck;
    }

    @Override
    public String toString() {
        return "Payment{"
                + "id=" + id
                + ", customer=" + (customer != null ? customer.getId() : "null") // Evita carregar toda a entidade Customer
                + ", paymentMethod=" + paymentMethod
                + ", amount=" + amount
                + ", currency='" + currency + '\''
                + ", status=" + status
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + ", fraudCheck=" + (fraudCheck != null ? fraudCheck.getId() : "null") // Evita carregar toda a entidade FraudCheck
                + '}';
    }

}
