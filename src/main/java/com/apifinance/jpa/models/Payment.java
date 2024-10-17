package com.apifinance.jpa.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.apifinance.jpa.enums.FraudCheckResult;
import com.apifinance.jpa.enums.PaymentMethodType;
import com.apifinance.jpa.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
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

    @JsonBackReference
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<FraudCheck> fraudChecks = new ArrayList<>();

    

    public Payment() {}

    public Payment(Customer customer, PaymentMethodType paymentMethod, BigDecimal amount, String currency) {
        this.customer = customer;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.currency = currency;
    }

    // Método para adicionar uma verificação de fraude
    public void addFraudCheck(FraudCheck fraudCheck) {
        fraudChecks.add(fraudCheck);
        fraudCheck.setPayment(this);
    }

    // Getters e Setters
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

    public List<FraudCheck> getFraudChecks() {
        return fraudChecks; // Método para acessar a lista de verificações de fraude
    }

    public RabbitMQMessage getRabbitMQMessage() {
        return rabbitMQMessage;
    }

    public void setRabbitMQMessage(RabbitMQMessage rabbitMQMessage) {
        this.rabbitMQMessage = rabbitMQMessage;
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
                ", fraudChecksCount=" + fraudChecks.size() + // Adicionando contagem de verificações de fraude
                '}';
    }

    @PrePersist
    public void prePersist() {
        this.status = PaymentStatus.PENDING; // Define o status como PENDING ao persistir
    }

    @PreUpdate
    public void preUpdate() {
        // Se o status de pagamento for PENDING, verificar se existem checagens de fraude
        if (this.status == PaymentStatus.PENDING) {
            // Verifique se há checagens de fraude e se todas são aprovadas
            boolean allApproved = fraudChecks.stream()
                .allMatch(fraudCheck -> fraudCheck.getFraudStatus() == FraudCheckResult.APPROVED);
            
            if (allApproved) {
                this.status = PaymentStatus.APPROVED; // Altera o status para APPROVED
            } else {
                this.status = PaymentStatus.REJECTED; // Ou qualquer lógica para rejeição
            }
        }
    }
}
