package com.apifinance.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apifinance.jpa.models.RabbitMqMessage;

public interface RabbitMqMessageRepository extends JpaRepository<RabbitMqMessage, Long> {
    // Método personalizado para encontrar uma mensagem pelo conteúdo
    Optional<RabbitMqMessage> findByMessageContent(String messageContent);
    
    // Método personalizado para verificar se uma mensagem existe pelo conteúdo
    boolean existsByMessageContent(String messageContent);
}
