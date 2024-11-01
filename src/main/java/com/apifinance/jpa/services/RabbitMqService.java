package com.apifinance.jpa.services;

import com.apifinance.jpa.enums.RabbitMqMessageStatus;
import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.repositories.RabbitMqMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

        RabbitMqMessage rabbitMqMessage = new RabbitMqMessage();
        rabbitMqMessage.setMessageContent(messageContent);
        rabbitMqMessage.setStatus(RabbitMqMessageStatus.SENT);

        ZonedDateTime sentAt = ZonedDateTime.now();
        rabbitMqMessage.setSentAt(sentAt);
        rabbitRepository.save(rabbitMqMessage);
        logger.info("Mensagem criada com status SENT e registrada na tabela rabbitmq_message: {}", rabbitMqMessage);
        logger.info("Hora de envio (sent_at): {}", sentAt);

        try {

            rabbitTemplate.convertAndSend("paymentQueue", messageContent);
            logger.info("Mensagem publicada na fila RabbitMQ: {}", messageContent);

            rabbitMqMessage.setStatus(RabbitMqMessageStatus.PROCESSED);
            ZonedDateTime processedAt = ZonedDateTime.now();
            rabbitMqMessage.setProcessedAt(processedAt);
            rabbitRepository.save(rabbitMqMessage);

            logger.info("Registro atualizado para status PROCESSED na tabela rabbitmq_message: {}", rabbitMqMessage);
            logger.info("Hora de processamento (processed_at): {}", processedAt);

        } catch (DataAccessException | AmqpException e) {

            rabbitMqMessage.setStatus(RabbitMqMessageStatus.ERROR);
            ZonedDateTime processedAt = ZonedDateTime.now();
            rabbitMqMessage.setProcessedAt(processedAt);
            rabbitRepository.save(rabbitMqMessage);
            logger.error("Erro ao publicar mensagem no RabbitMQ. Status atualizado para ERROR: {}", rabbitMqMessage, e);
            logger.info("Hora de erro (processed_at): {}", processedAt);
        }

    }
}
