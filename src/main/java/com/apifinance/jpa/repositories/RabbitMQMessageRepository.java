package com.apifinance.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apifinance.jpa.models.RabbitMqMessage;

@Repository
public interface RabbitMqMessageRepository extends JpaRepository<RabbitMqMessage, Long> {
    // Você pode adicionar consultas personalizadas aqui, se necessário
}
