package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.enums.RabbitMqMessageStatus;
import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.repositories.RabbitMqMessageRepository;

@Service
public class RabbitMqService {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqMessageRepository rabbitMqMessageRepository;

    @Autowired
    public RabbitMqService(RabbitTemplate rabbitTemplate, RabbitMqMessageRepository rabbitMqMessageRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMqMessageRepository = rabbitMqMessageRepository;
    }

    // Método para publicar a mensagem e salvar no repositório
    public void publishMessage(String exchange, String routingKey, String messageContent) {
        // Enviar a mensagem
        rabbitTemplate.convertAndSend(exchange, routingKey, messageContent);
        
        // Criar e salvar a mensagem no repositório
        RabbitMqMessage rabbitMqMessage = new RabbitMqMessage();
        rabbitMqMessage.setMessageContent(messageContent);
        rabbitMqMessage.setStatus(RabbitMqMessageStatus.SENT);
        rabbitMqMessage.setProcessedAt(ZonedDateTime.now());
        rabbitMqMessage.setSentAt(ZonedDateTime.now());
        rabbitMqMessageRepository.save(rabbitMqMessage);
    }

    // Método para encontrar todas as mensagens
    public List<RabbitMqMessage> findAll() {
        return rabbitMqMessageRepository.findAll();
    }

    // Método para encontrar uma mensagem por ID
    public RabbitMqMessage findById(Long id) {
        return rabbitMqMessageRepository.findById(id).orElse(null);
    }

    // Método para deletar uma mensagem por ID
    public void deleteById(Long id) {
        rabbitMqMessageRepository.deleteById(id);
    }
}
