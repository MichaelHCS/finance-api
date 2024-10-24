package com.apifinance.jpa.models;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

//import com.apifinance.jpa.enums.FraudCheckReason;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentType paymentType;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fraud_check_id")
    private FraudCheck fraudCheck;

    @Transient
    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentMethod paymentMethod; 

    @Transient
    @OneToOne(fetch = FetchType.LAZY)
    private RabbitMqMessage rabbitMqMessage;
    //@Transient
    //private FraudCheckReason checkReason;

    public Payment() {
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();

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

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
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

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    /*public FraudCheckReason getCheckReason() {
        return checkReason;
    }

    public void setCheckReason(FraudCheckReason checkReason) {
        this.checkReason = checkReason;
    }
    */

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public RabbitMqMessage getRabbitMqMessage() {
        return rabbitMqMessage;
    }

    public void setRabbitMqMessage(RabbitMqMessage rabbitMqMessage) {
        this.rabbitMqMessage = rabbitMqMessage;
    }

    @Override
    public String toString() {
        return "Payment{"
                + "id=" + id
                + ", customer=" + (customer != null ? customer.getId() : "null")
                + ", paymentMethod=" + paymentType
                + ", amount=" + amount
                + ", currency='" + currency + '\''
                + ", status=" + paymentStatus
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + ", fraudCheck=" + (fraudCheck != null ? fraudCheck.getId() : "null")
                + '}';
    

    }

   

}
