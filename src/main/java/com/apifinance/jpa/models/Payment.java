package com.apifinance.jpa.models;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import com.apifinance.jpa.enums.PaymentMethodType;
import com.apifinance.jpa.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "payment")
public final class Payment extends BaseEntity { // Estendendo a BaseEntity

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

    @Column(nullable = false, length = 3)
    @NotNull
    @Pattern(regexp = "[A-Z]{3}", message = "A moeda deve estar no formato ISO 4217 (ex: BRL, USD)")
    private String currency;

    @ManyToOne
    @JoinColumn(name = "fraud_check_id")
    private FraudCheck fraudCheck;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull
    private PaymentStatus status = PaymentStatus.PENDING; // Status padrão

    // Construtor padrão (necessário para o JPA)
    public Payment() {}

    // Construtor com parâmetros
    public Payment(Customer customer, PaymentMethodType paymentMethod, BigDecimal amount, String currency) {
        this.customer = customer;
        this.paymentMethod = paymentMethod;
        setAmount(amount); // Use o setter para aplicar validação
        setCurrency(currency); // Use o setter para aplicar validação
        onCreate(); // Chama o método da classe pai para definir as datas
    }

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate(); // Chama o método da classe pai
    }

    @PreUpdate
    @Override
    protected void onUpdate() {
        super.onUpdate(); // Chama o método da classe pai
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
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor deve ser positivo e não nulo");
        }
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        // Validação manual da moeda
        if (currency == null || currency.trim().isEmpty() || !currency.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("A moeda deve estar no formato ISO 4217 (ex: BRL, USD) e não pode ser vazia");
        }
        this.currency = currency;
    }

    public FraudCheck getFraudCheck() {
        return fraudCheck;
    }

    public void setFraudCheck(FraudCheck fraudCheck) {
        this.fraudCheck = fraudCheck;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    // Método para exibir a data formatada
    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return createdAt.format(formatter);
    }

    public String getFormattedUpdatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return updatedAt.format(formatter);
    }
}
