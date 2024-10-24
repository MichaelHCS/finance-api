package com.apifinance.jpa.repositories;

import com.apifinance.jpa.models.RabbitMqMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RabbitMqMessageRepository extends JpaRepository<RabbitMqMessage, Long> {
    // Você pode adicionar métodos personalizados aqui, se necessário
}
