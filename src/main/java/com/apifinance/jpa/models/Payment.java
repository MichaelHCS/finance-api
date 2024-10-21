package com.apifinance.jpa.models;

import java.time.ZonedDateTime;

import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.enums.PaymentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Gera o ID automaticamente
    private Long id; // ID do pagamento

    @ManyToOne // Relacionamento ManyToOne com Customer
    @JoinColumn(name = "customer_id", nullable = false) // Nome da coluna no banco de dados
    @NotNull // Garantir que um cliente esteja sempre associado
    private Customer customer; // Cliente associado ao pagamento

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false) // Método de pagamento
    @NotNull
    private PaymentType paymentType;

    @Column(name = "amount", nullable = false) // Valor do pagamento
    @NotNull
    private Double amount;

    @Column(name = "currency", nullable = false) // Moeda do pagamento
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false) // Status do pagamento
    @NotNull
    private PaymentStatus paymentStatus;

    @Column(name = "created_at", nullable = false) // Data e hora da criação do pagamento
    private final ZonedDateTime createdAt;

    @Column(name = "updated_at") // Data e hora da última atualização do status
    private ZonedDateTime updatedAt;

    @Column(name = "fraud_check_id") // ID da análise de fraude associada
    private Long fraudCheckId; // Assumindo que é um FK simples

    //@Transient
   // @ManyToOne
    //@JoinColumn(name = "payment_method_id", nullable = false)
    //private PaymentMethod paymentMethod;

    //@Transient
    //@ManyToOne
    //@JoinColumn(name = "rabbitmq_message_id")
    //private RabbitMqMessage rabbitMqMessage;

    // Construtor padrão
    public Payment() {
        this.createdAt = ZonedDateTime.now(); // Inicializa createdAt com a data e hora atual
        this.updatedAt = null; // Inicializa updatedAt como null
    }

    // Construtor com parâmetros
    public Payment(Customer customer, PaymentType paymentType, Double amount, String currency, PaymentStatus paymentStatus, Long fraudCheckId) {
        this.customer = customer;
        this.paymentType = paymentType;
        this.amount = amount;
        this.currency = currency;
        this.paymentStatus = paymentStatus;
        this.createdAt = ZonedDateTime.now(); // Define a data de criação
        this.updatedAt = null; // Inicializa updatedAt como null
        this.fraudCheckId = fraudCheckId; // Define o ID da análise de fraude
    }

    // Getters e Setters para os campos
    public Long getId() {
        return id;
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

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt; // Getter para createdAt
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt; // Getter para updatedAt
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt; // Setter para updatedAt
    }

    public Long getFraudCheckId() {
        return fraudCheckId; // Getter para fraudCheckId
    }

    public void setFraudCheckId(Long fraudCheckId) {
        this.fraudCheckId = fraudCheckId; // Setter para fraudCheckId
    }

    //public PaymentMethod getPaymentMethod() {
    //    return paymentMethod;
    //}

    //public void setPaymentMethod(PaymentMethod paymentMethod) {
     //   this.paymentMethod = paymentMethod;
    //}

    //public RabbitMqMessage getRabbitMqMessage() {
    //    return rabbitMqMessage;
    //}

    //public void setRabbitMqMessage(RabbitMqMessage rabbitMqMessage) {
    //    this.rabbitMqMessage = rabbitMqMessage;
    //}

    @Override
    public String toString() {
        return "Payment{"
                + "id=" + id
                + ", customerId=" + (customer != null ? customer.getId() : null) // Inclui o ID do cliente
                + ", paymentType='" + paymentType + '\''
                + ", amount=" + amount
                + ", currency='" + currency + '\''
                + ", status='" + paymentStatus + '\''
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + ", fraudCheckId=" + fraudCheckId
                + '}';
    }

}
