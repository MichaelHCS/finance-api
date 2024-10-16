package com.apifinance.jpa.models;

import java.math.BigDecimal;

import com.apifinance.jpa.enums.PaymentMethodType;
import com.apifinance.jpa.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
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
    @Column(name = "payment_method", nullable = false)
    @NotNull
    private PaymentMethodType paymentMethod;

    @DecimalMin(value = "0.0", message = "O valor deve ser positivo")
    @Column(nullable = false)
    @NotNull
    private BigDecimal amount;

    @NotNull
    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private PaymentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fraud_check_id")
    private FraudCheck fraudCheck;

    @ManyToOne // Define um relacionamento ManyToOne com RabbitMQMessage
    @JoinColumn(name = "rabbitmq_message_id", nullable = false) // Coluna que referencia RabbitMQMessage
    private RabbitMQMessage rabbitMQMessage; // Referência à entidade RabbitMQMessage

    public Payment() {}

    public Payment(Customer customer, PaymentMethodType paymentMethod, BigDecimal amount, String currency, FraudCheck fraudCheck) {
        this.customer = customer;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.currency = currency;
        this.fraudCheck = fraudCheck;
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
                ", customerId=" + (customer != null ? customer.getId() : null) +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", status=" + status +
                ", paymentMethod=" + paymentMethod +
                ", fraudCheckId=" + (fraudCheck != null ? fraudCheck.getId() : null) +
                '}';
    }

    @PrePersist
    public void prePersist() {
        this.status = PaymentStatus.PENDING;
    }

    @PreUpdate
    public void preUpdate() {}

    public RabbitMQMessage getRabbitMQMessage() {
        return rabbitMQMessage;
    }

    public void setRabbitMQMessage(RabbitMQMessage rabbitMQMessage) {
        this.rabbitMQMessage = rabbitMQMessage;
    }
}
