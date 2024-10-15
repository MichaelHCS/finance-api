package com.apifinance.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apifinance.jpa.enums.rabbitmqMessageStatus;
import com.apifinance.jpa.models.RabbitMQMessage;

public interface RabbitMQMessageRepository extends JpaRepository<RabbitMQMessage, Long> {

    public RabbitMQMessage findFirstByStatusOrderBySentAtAsc(rabbitmqMessageStatus rabbitmqMessageStatus);
}
