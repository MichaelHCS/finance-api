package com.apifinance.jpa.models;

import java.time.ZonedDateTime;
//import java.util.List;
import com.apifinance.jpa.enums.RabbitMqMessageStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "rabbitmq_message")
public class RabbitMqMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_content", nullable = false)
    private String messageContent;

    @Column(name = "status", nullable = false)
    private RabbitMqMessageStatus status;

    @Column(name = "sent_at", nullable = false)
    private ZonedDateTime sentAt;

    @Column(name = "processed_at")
    private ZonedDateTime processedAt;

    //@OneToMany(mappedBy = "rabbitMqMessage", fetch = FetchType.LAZY)
    //private List<Payment> payments;

    public RabbitMqMessage() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public RabbitMqMessageStatus getStatus() {
        return status;
    }

    public void setStatus(RabbitMqMessageStatus status) {
        this.status = status;
    }

    public ZonedDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public ZonedDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(ZonedDateTime processedAt) {
        this.processedAt = processedAt;
    }

    //public List<Payment> getPayments() {
    //    return payments;
    //}

    //public void setPayments(List<Payment> payments) {
    //    this.payments = payments;
    //}
}
