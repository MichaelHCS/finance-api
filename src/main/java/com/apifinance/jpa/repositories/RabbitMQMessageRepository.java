package com.apifinance.jpa.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apifinance.jpa.models.RabbitMqMessage;

public interface RabbitMqMessageRepository extends JpaRepository<RabbitMqMessage, UUID> {
  
    Optional<RabbitMqMessage> findByMessageContent(String messageContent);
    
   
    boolean existsByMessageContent(String messageContent);
}
