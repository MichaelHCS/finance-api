package com.apifinance.jpa.services;

//import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.apifinance.jpa.enums.RabbitMqMessageStatus;
import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.repositories.RabbitMqMessageRepository;

@Service
public class RabbitMqService {

    private final RabbitTemplate rabbitTemplate; // Declaração do RabbitTemplate

    private final RabbitMqMessageRepository rabbitMqMessageRepository;

    @Autowired
    public RabbitMqService(RabbitTemplate rabbitTemplate, RabbitMqMessageRepository rabbitMqMessageRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMqMessageRepository = rabbitMqMessageRepository;
    }

    public void sendMessage(String exchange, String routingKey, String messageContent) {
        rabbitTemplate.convertAndSend(exchange, routingKey, messageContent);
    }

    public List<RabbitMqMessage> findAll() {
        return rabbitMqMessageRepository.findAll();
    }

    public RabbitMqMessage findById(Long id) {
        return rabbitMqMessageRepository.findById(id).orElse(null);
    }

    public RabbitMqMessage save(RabbitMqMessage rabbitMqMessage) {
        return rabbitMqMessageRepository.save(rabbitMqMessage);
    }

    public void deleteById(Long id) {
        rabbitMqMessageRepository.deleteById(id);
    }
}
