package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.enums.RabbitMqMessageStatus;
import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.repositories.RabbitMqMessageRepository;

import java.util.UUID;

@Service
public class RabbitMqService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqService.class);

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqMessageRepository rabbitMqMessageRepository;

    @Autowired
    public RabbitMqService(RabbitTemplate rabbitTemplate, RabbitMqMessageRepository rabbitMqMessageRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMqMessageRepository = rabbitMqMessageRepository;
    }

    public void publishMessage(String exchange, String routingKey, String messageContent) {
    // Verifica se a mensagem já existe no repositório
    Optional<RabbitMqMessage> existingMessageOpt = rabbitMqMessageRepository.findByMessageContent(messageContent);

    if (existingMessageOpt.isPresent()) {
        System.out.println("A mensagem já existe no repositório e não será enviada novamente.");
        return;
    }

    if (messageContent == null || messageContent.isEmpty()) {
        System.out.println("O conteúdo da mensagem não pode ser nulo ou vazio.");
        return;
    }

    RabbitMqMessage rabbitMqMessage = new RabbitMqMessage();
    rabbitMqMessage.setMessageContent(messageContent);
    rabbitMqMessage.setSentAt(ZonedDateTime.now());

    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
    try {
        rabbitTemplate.setConfirmCallback((correlationData1, ack, cause) -> {
            if (ack) {
                System.out.println("Mensagem confirmada pelo RabbitMQ.");
                rabbitMqMessage.setStatus(RabbitMqMessageStatus.SENT);
                processMessage(rabbitMqMessage);
            } else {
                System.err.println("Falha no envio da mensagem: " + cause);
                rabbitMqMessage.setStatus(RabbitMqMessageStatus.ERROR);
            }
            rabbitMqMessageRepository.save(rabbitMqMessage);
        });

        rabbitTemplate.convertAndSend(exchange, routingKey, messageContent, correlationData);
        System.out.println("Mensagem enviada para RabbitMQ com sucesso.");

    } catch (AmqpException e) {
        System.err.println("Erro ao enviar mensagem via RabbitMQ: " + e.getMessage());
        rabbitMqMessage.setStatus(RabbitMqMessageStatus.ERROR);
        rabbitMqMessageRepository.save(rabbitMqMessage);
    }
}


    public void processMessage(RabbitMqMessage rabbitMqMessage) {
        try {
            if (rabbitMqMessage.getMessageContent() == null) {
                throw new IllegalArgumentException("Conteúdo da mensagem não pode ser nulo");
            }

            logger.info("Processando a mensagem: {}", rabbitMqMessage.getMessageContent());

            rabbitMqMessage.setProcessedAt(ZonedDateTime.now());
            rabbitMqMessage.setStatus(RabbitMqMessageStatus.PROCESSED);
            rabbitMqMessageRepository.save(rabbitMqMessage);

        } catch (IllegalArgumentException | DataAccessException e) {
            logger.error("Erro ao processar a mensagem: {}", e.getMessage());
            rabbitMqMessage.setStatus(RabbitMqMessageStatus.ERROR);
            rabbitMqMessage.setProcessedAt(ZonedDateTime.now());
            rabbitMqMessageRepository.save(rabbitMqMessage);
        }
    }
}
