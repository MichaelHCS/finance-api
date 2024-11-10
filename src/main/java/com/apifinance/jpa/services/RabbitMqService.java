package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.enums.RabbitMqMessageStatus;
import com.apifinance.jpa.exceptions.ResourceNotFoundException;
import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.repositories.RabbitMqMessageRepository;

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
        RabbitMqMessage rabbitMqMessage = rabbitRepository.findByMessageContent(messageContent)
        .orElseGet(() -> {
            RabbitMqMessage newMessage = new RabbitMqMessage();
            newMessage.setMessageContent(messageContent);
            newMessage.setStatus(RabbitMqMessageStatus.SENT);
            newMessage.setSentAt(ZonedDateTime.now());
            return newMessage;
        });

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

    public RabbitMqMessage updateMessage(UUID id, RabbitMqMessage updatedMessage) {
        RabbitMqMessage existingMessage = getMessageById(id);
        existingMessage.setMessageContent(updatedMessage.getMessageContent());
        existingMessage.setStatus(updatedMessage.getStatus());
        existingMessage.setProcessedAt(updatedMessage.getProcessedAt() != null ? updatedMessage.getProcessedAt() : existingMessage.getProcessedAt());
        
        RabbitMqMessage updatedRabbitMqMessage = rabbitRepository.save(existingMessage);
        logger.info("Mensagem atualizada com ID {}: {}", id, updatedRabbitMqMessage);
        return updatedRabbitMqMessage;
    }

    public List<RabbitMqMessage> getAllMessages() {
        List<RabbitMqMessage> messages = rabbitRepository.findAll();
        logger.info("Total de mensagens recuperadas: {}", messages.size());
        return messages;
    }

    public RabbitMqMessage getMessageById(UUID id) {
        RabbitMqMessage message = rabbitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mensagem não encontrada com ID: " + id));
        logger.info("Mensagem recuperada com ID {}: {}", id, message);
        return message;
    }
}
