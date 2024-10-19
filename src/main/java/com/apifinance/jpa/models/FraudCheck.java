package com.apifinance.jpa.models;

import java.time.ZonedDateTime;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckResult;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "fraud_check") 
public class FraudCheck extends BaseEntity {

    @NotNull 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false) 
    @JsonBackReference
    private Payment payment; 

    @Enumerated(EnumType.STRING)
    @NotNull 
    @Column(name = "fraud_status", nullable = false) 
    private FraudCheckResult fraudStatus; 

    @Enumerated(EnumType.STRING)
    @Column(name = "check_reason") 
    private FraudCheckReason checkReason; 

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rabbitmq_message_id", nullable = false) 
    private RabbitMQMessage rabbitmqMessage; 
    
    public FraudCheck() {}

    
    public FraudCheck(Payment payment, FraudCheckResult fraudStatus, FraudCheckReason checkReason, RabbitMQMessage rabbitmqMessage) {
        this.payment = payment;
        this.fraudStatus = fraudStatus;
        this.checkReason = checkReason;
        this.checkedAt = ZonedDateTime.now();
        this.rabbitmqMessage = rabbitmqMessage;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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

    public RabbitMQMessage getRabbitmqMessage() {
        return rabbitmqMessage;
    }

    public void setRabbitmqMessage(RabbitMQMessage rabbitmqMessage) {
        this.rabbitmqMessage = rabbitmqMessage;
    }

    @Override
    public String toString() {
        return "FraudCheck{" +
                "id=" + getId() +  
                //", paymentId=" + (payment != null ? payment.getId() : null)* + 
                ", fraudStatus=" + fraudStatus +
                ", checkReason=" + checkReason +
                ", checkedAt=" + getCheckedAt() +
               
            
                '}';
    }

 
        
}
