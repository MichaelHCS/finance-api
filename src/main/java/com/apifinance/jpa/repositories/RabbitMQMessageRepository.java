package com.apifinance.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apifinance.jpa.models.RabbitMqMessage;

public interface RabbitMqMessageRepository extends JpaRepository<RabbitMqMessage, Long> {
    // Custom query method to find a message by content
    Optional<RabbitMqMessage> findByMessageContent(String messageContent);
}
