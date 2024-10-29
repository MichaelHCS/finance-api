package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

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
        Optional<RabbitMqMessage> existingMessageOpt = rabbitMqMessageRepository.findByMessageContent(messageContent);
        
        if (existingMessageOpt.isPresent()) {
            System.out.println("A mensagem já existe no repositório e não será enviada novamente.");
            return; // Não envia a mensagem se já existe
        }
    
        RabbitMqMessage rabbitMqMessage = new RabbitMqMessage();
        rabbitMqMessage.setMessageContent(messageContent);
        rabbitMqMessage.setSentAt(ZonedDateTime.now()); // Define o timestamp no início
    
        try {
            // Enviar a mensagem
            rabbitTemplate.convertAndSend(exchange, routingKey, messageContent);
            
            // Atualiza o status para SENT após envio bem-sucedido
            rabbitMqMessage.setStatus(RabbitMqMessageStatus.SENT);
            
            // Processa a mensagem após confirmar o envio
            processMessage(rabbitMqMessage);
            
            System.out.println("Mensagem enviada e processada com sucesso.");
        } catch (Exception e) {
            // Atualiza o status para ERROR em caso de falha
            System.err.println("Erro ao enviar mensagem: " + e.getMessage());
            rabbitMqMessage.setStatus(RabbitMqMessageStatus.ERROR);
        } finally {
            // Salvar o registro, mesmo em caso de erro
            rabbitMqMessageRepository.save(rabbitMqMessage);
        }
    }
    

    // Método para processar a mensagem com tratamento de erro
    public void processMessage(RabbitMqMessage rabbitMqMessage) {
        try {
            if (rabbitMqMessage.getMessageContent() == null) {
                throw new IllegalArgumentException("Conteúdo da mensagem não pode ser nulo");
            }

            // Simulação de processamento bem-sucedido
            System.out.println("Processando a mensagem: " + rabbitMqMessage.getMessageContent());

            // Atualiza o status e define o processedAt
            rabbitMqMessage.setProcessedAt(ZonedDateTime.now());
            rabbitMqMessage.setStatus(RabbitMqMessageStatus.PROCESSED);

            // Salva as alterações sem criar um novo registro
            rabbitMqMessageRepository.save(rabbitMqMessage);
        } catch (Exception e) {
            System.err.println("Erro ao processar a mensagem: " + e.getMessage());

            // Atualiza o status para ERROR se algo der errado
            rabbitMqMessage.setStatus(RabbitMqMessageStatus.ERROR);
            rabbitMqMessage.setProcessedAt(ZonedDateTime.now()); // Define o processedAt mesmo em caso de erro
            rabbitMqMessageRepository.save(rabbitMqMessage);
        }
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
