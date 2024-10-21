package com.apifinance.jpa.models;

//import java.util.Set;

import com.apifinance.jpa.enums.PaymentMethodDetails;
import com.apifinance.jpa.enums.PaymentType;


//import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
//import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "payment_method")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Gera o ID automaticamente
    private Long id; // ID do método de pagamento

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @NotNull
    private PaymentType type; // Tipo de pagamento (ex: Cartão de crédito, Boleto)

    @Enumerated(EnumType.STRING)
    @Column(name = "details", nullable = false)
    @NotNull
    @Size(min = 1, max = 255)
    private PaymentMethodDetails details;

    //@OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   // private Set<Payment> payments;

    // Construtor padrão
    public PaymentMethod() {
    }

    // Construtor com parâmetros
    public PaymentMethod(PaymentType type, PaymentMethodDetails details) {
        this.type = type;
        this.details = details;
    }

    // Getters e Setters
    public Long getId() {
        return id; // Getter para o ID
    }

    public void setId(Long id) {
        this.id = id; // Setter para o ID
    }

    public PaymentType getType() {
        return type; // Getter para type
    }

    public void setType(PaymentType type) {
        this.type = type; // Setter para type
    }

    public PaymentMethodDetails getDetails() {
        return details; // Getter para details
    }

    public void setDetails(PaymentMethodDetails details) {
        this.details = details; // Setter para details
    }

    //public Set<Payment> getPayments() {
    //    return payments;
    //}

    //public void setPayments(Set<Payment> payments) {
   //     this.payments = payments;
    //}

    @Override
    public String toString() {
        return "PaymentMethod{"
                + "id=" + id // Inclui o ID na representação de string
                + ", type='" + type + '\'' // Inclui o tipo na representação de string
                + ", details='" + details + '\'' // Inclui detalhes na representação de string
                + '}';
    }

}
