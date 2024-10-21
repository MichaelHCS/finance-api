package com.apifinance.jpa.rabbitmqconfig;

import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.repositories.RabbitMqMessageRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import com.apifinance.jpa.enums.RabbitMqMessageStatus;

@Component
public class RabbitMQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @Autowired
    private RabbitMqMessageRepository messageRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void consumeMessage(String messageContent) {
        logger.info("Mensagem recebida da fila '{}': {}", RabbitConfig.QUEUE_NAME, messageContent);

        // Verifica se a mensagem está vazia ou nula
        if (messageContent == null || messageContent.trim().isEmpty()) {
            logger.error("Mensagem vazia recebida, descartando.");
            return;
        }

        RabbitMqMessage rabbitMqMessage = new RabbitMqMessage();
        rabbitMqMessage.setMessageContent(messageContent);
        rabbitMqMessage.setStatus(RabbitMqMessageStatus.SENT); // Atualizando para usar enum
        rabbitMqMessage.setSentAt(ZonedDateTime.now());
        messageRepository.save(rabbitMqMessage);

        try {
            processMessage(rabbitMqMessage);
            rabbitMqMessage.setStatus(RabbitMqMessageStatus.PROCESSED); // Atualizando para usar enum
            rabbitMqMessage.setProcessedAt(ZonedDateTime.now());
            messageRepository.save(rabbitMqMessage);

            logger.info("Mensagem processada com sucesso: {}", messageContent);
        } catch (InterruptedException | RuntimeException e) {
            handleProcessingError(messageContent, rabbitMqMessage, e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao processar a mensagem: {}. Enviando para a DLQ.", messageContent, e);
            handleProcessingError(messageContent, rabbitMqMessage, e);
        }
    }

    private void handleProcessingError(String messageContent, RabbitMqMessage rabbitMqMessage, Exception e) {
        logger.error("Erro ao processar a mensagem: {}. Enviando para a DLQ.", messageContent, e);
        rabbitMqMessage.setStatus(RabbitMqMessageStatus.ERROR); // Atualizando para usar enum
        rabbitMqMessage.setProcessedAt(ZonedDateTime.now()); // Atualizando para a data de erro
        messageRepository.save(rabbitMqMessage);
        sendToDeadLetterQueue(messageContent);
    }

    private void processMessage(RabbitMqMessage rabbitMqMessage) throws InterruptedException {
        logger.info("Iniciando o processamento da mensagem: {}. Aguardando 30 segundos...", rabbitMqMessage.getMessageContent());
        Thread.sleep(30000);
        logger.info("Processando a mensagem: {}", rabbitMqMessage.getMessageContent());
        if (rabbitMqMessage.getMessageContent().contains("error")) {
            throw new RuntimeException("Erro simulado no processamento da mensagem.");
        }
    }

    private void sendToDeadLetterQueue(String messageContent) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.PAYMENT_ROUTING_KEY + ".dlq", messageContent);
        logger.info("Mensagem enviada para a DLQ: {}", messageContent);
    }
}
