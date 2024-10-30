package com.apifinance.jpa.configrabbitmq;

import java.time.ZonedDateTime;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.services.RabbitMqService;

@Component
public class RabbitMqListener {

    private final RabbitMqService rabbitMqService;

    @Autowired
    public RabbitMqListener(RabbitMqService rabbitMqService) {  

        this.rabbitMqService = rabbitMqService;
    }

    @RabbitListener(queues = RabbitMqConfig.PAYMENT_QUEUE, ackMode = "MANUAL")
    public void receiveMessage(Message message) {
        // Extrai o conteúdo da mensagem recebida
        String messageContent = new String(message.getBody());

        // Cria uma nova instância de RabbitMqMessage
        RabbitMqMessage rabbitMqMessage = new RabbitMqMessage();
        rabbitMqMessage.setMessageContent(messageContent);
        rabbitMqMessage.setSentAt(ZonedDateTime.now());

        // Processa a mensagem através do RabbitMqService
        try {
            rabbitMqService.processMessage(rabbitMqMessage);
            System.out.println("Mensagem processada com sucesso: " + messageContent);
        } catch (Exception e) {
            // Se ocorrer um erro, o acknowledgment automático impede o reconhecimento
            System.err.println("Erro ao processar a mensagem: " + e.getMessage());
            throw e; // Exceção lançada impede o acknowledgment automático
        }
    }
}
