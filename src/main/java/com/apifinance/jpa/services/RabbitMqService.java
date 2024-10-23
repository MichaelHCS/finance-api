package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.enums.RabbitMqMessageStatus;
import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.repositories.RabbitMqMessageRepository;

@Service
public class RabbitMqService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitMqMessageRepository rabbitMqMessageRepository;

    // Envia mensagem ao RabbitMQ e salva no banco de dados
    public void sendMessage(String exchange, String routingKey, String messageContent) {
        RabbitMqMessage rabbitMqMessage = new RabbitMqMessage();
        rabbitMqMessage.setMessageContent(messageContent);
        rabbitMqMessage.setSentAt(ZonedDateTime.now()); // Define a data/hora de envio
    
        try {
            // Tenta enviar a mensagem para o RabbitMQ
            amqpTemplate.convertAndSend(exchange, routingKey, messageContent);
    
            // Verifica o status da mensagem e define o comportamento
            if (messageContent.contains("error")) {
                // Simulação de um cenário de erro, define o status como ERROR
                rabbitMqMessage.setStatus(RabbitMqMessageStatus.ERROR);
            } else {
                // Mensagem enviada com sucesso, define o status como SENT
                rabbitMqMessage.setStatus(RabbitMqMessageStatus.SENT);
            }
    
            // Após enviar a mensagem, podemos fazer algo como atualizar uma tabela para "PROCESSADO"
            if (rabbitMqMessage.getStatus() == RabbitMqMessageStatus.SENT) {
                // Simula que o processamento ocorreu com sucesso
                rabbitMqMessage.setStatus(RabbitMqMessageStatus.PROCESSED);
            }
    
        } catch (Exception e) {
            // Em caso de erro ao enviar, define o status como ERROR
            rabbitMqMessage.setStatus(RabbitMqMessageStatus.ERROR);
        }
    
        // Salva o registro da mensagem no banco de dados, com o status apropriado
        rabbitMqMessageRepository.save(rabbitMqMessage);
    }
    

    // Métodos CRUD para a tabela rabbitmq_message
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
