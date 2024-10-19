package com.apifinance.jpa.models;

import java.math.BigDecimal;
import com.apifinance.jpa.enums.PaymentMethodType;
import com.apifinance.jpa.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "payment")
public final class Payment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "payment_method", nullable = false)
    private PaymentMethodType paymentMethod;

    @DecimalMin(value = "0.0", message = "O valor deve ser positivo")
    @NotNull
    @Column(nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private PaymentStatus status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rabbitmq_message_id")
    private RabbitMQMessage rabbitMQMessage;

    @JsonIgnore
    @ManyToOne 
    @JoinColumn(name = "fraud_check_id",  nullable = true) 
    private FraudCheck fraudCheck;

    @Transient
    @Column(name = "check_reason") 
    private String checkReason;

    public Payment() {}

    public Payment(Customer customer, PaymentMethodType paymentMethod, BigDecimal amount, String currency) {
        this.customer = customer;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.currency = currency;
    }

    public Customer getCustomer() {
        return customer;
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

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public RabbitMQMessage getRabbitMQMessage() {
        return rabbitMQMessage;
    }

    public void setRabbitMQMessage(RabbitMQMessage rabbitMQMessage) {
        this.rabbitMQMessage = rabbitMQMessage;
    }

    public String getCheckReason() {
        return checkReason;
    }

    public void setCheckReason(String checkReason) {
        this.checkReason = checkReason;
    }


    public FraudCheck getFraudCheck() {
       return fraudCheck;
    }

    public void setFraudCheck(FraudCheck fraudCheck) {
        this.fraudCheck = fraudCheck;
    }


    @Override
    public String toString() {
        return "Payment{" +
                "id=" + getId() +
                //", customerId=" + (customer != null ? customer.getId() : null) +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", status=" + status +
                ", paymentMethod=" + paymentMethod +
                ", fraudCheckId=" + fraudCheck +
                '}';
   
    }   

}