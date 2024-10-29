package com.apifinance.jpa.configrabbitmq;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.repositories.RabbitMqMessageRepository;
import com.apifinance.jpa.services.RabbitMqService;

@Component
public class RabbitMqListener {

    private final RabbitMqService rabbitMqService;
    private final RabbitMqMessageRepository rabbitMqMessageRepository;

    @Autowired // Adicione a anotação @Autowired para injeção automática
    public RabbitMqListener(RabbitMqService rabbitMqService, RabbitMqMessageRepository rabbitMqMessageRepository) {
        this.rabbitMqService = rabbitMqService;
        this.rabbitMqMessageRepository = rabbitMqMessageRepository;
    }

    @RabbitListener(queues = RabbitMqConfig.PAYMENT_QUEUE)
    public void listen(String messageContent) {
        // Encontre a mensagem existente com base no conteúdo
        Optional<RabbitMqMessage> existingMessageOpt = rabbitMqMessageRepository.findByMessageContent(messageContent);
    
        RabbitMqMessage rabbitMqMessage;
        if (existingMessageOpt.isPresent()) {
            rabbitMqMessage = existingMessageOpt.get();
        } else {
            // Se não existir, cria uma nova mensagem
            rabbitMqMessage = new RabbitMqMessage();
            rabbitMqMessage.setMessageContent(messageContent);
            rabbitMqMessage.setSentAt(ZonedDateTime.now());
        }
    
        // Processar a mensagem
        rabbitMqService.processMessage(rabbitMqMessage);
        System.out.println("Mensagem recebida: " + messageContent);
    }
}
