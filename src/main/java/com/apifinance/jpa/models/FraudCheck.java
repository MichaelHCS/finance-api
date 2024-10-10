package com.apifinance.jpa.models;

import java.time.LocalDateTime;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckResult;

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
@Table(name = "fraud_check")
public class FraudCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column(name = "payment_id", nullable = false)
    private Long paymentId; 

    @Enumerated(EnumType.STRING)
    @Column(name = "fraud_status", nullable = false)
    private FraudCheckResult fraudStatus; 

    @Enumerated(EnumType.STRING)
    @Column(name = "check_reason", nullable = false)
    private FraudCheckReason checkReason; 

    @Column(name = "checked_at", nullable = false)
    private LocalDateTime checkedAt; 

    @Column(name = "rabbitmq_message_id")
    private Long rabbitmqMessageId; 

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Payment payment; 
    // Construtor padrão
    public FraudCheck() {
        this.checkedAt = LocalDateTime.now(); 
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public FraudCheckResult getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(FraudCheckResult fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public FraudCheckReason getCheckReason() {
        return checkReason;
    }

    public void setCheckReason(FraudCheckReason checkReason) {
        this.checkReason = checkReason;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(LocalDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }

    public Long getRabbitmqMessageId() {
        return rabbitmqMessageId;
    }

    public void setRabbitmqMessageId(Long rabbitmqMessageId) {
        this.rabbitmqMessageId = rabbitmqMessageId;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
