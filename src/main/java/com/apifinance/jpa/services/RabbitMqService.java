package com.apifinance.jpa.services;

import com.apifinance.jpa.enums.RabbitMqMessageStatus;
import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.repositories.RabbitMqMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class RabbitMqService {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqService.class);

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqMessageRepository rabbitRepository;

    @Autowired
    public RabbitMqService(RabbitTemplate rabbitTemplate, RabbitMqMessageRepository rabbitRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitRepository = rabbitRepository;
    }

    public void publishMessage(String messageContent) {
        // Publica a mensagem na fila RabbitMQ
        rabbitTemplate.convertAndSend("paymentQueue", messageContent);
        logger.info("Mensagem publicada na fila RabbitMQ: {}", messageContent);

        // Cria um registro correspondente na tabela rabbitmq_message
        RabbitMqMessage rabbitMqMessage = new RabbitMqMessage();
        rabbitMqMessage.setMessageContent(messageContent);
        rabbitMqMessage.setStatus(RabbitMqMessageStatus.SENT);
        rabbitMqMessage.setSentAt(ZonedDateTime.now());
        rabbitRepository.save(rabbitMqMessage);
        logger.info("Registro criado na tabela rabbitmq_message: {}", rabbitMqMessage);
    }
}
