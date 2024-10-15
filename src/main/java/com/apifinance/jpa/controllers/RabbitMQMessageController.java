package com.apifinance.jpa.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apifinance.jpa.models.RabbitMQMessage;
import com.apifinance.jpa.rabbitmqConfig.RabbitConfig;
import com.apifinance.jpa.repositories.RabbitMQMessageRepository;

@RestController
@RequestMapping("/rabbitmq_message")
public class RabbitMQMessageController {

    @Autowired
    private RabbitMQMessageRepository rabbitMQMessageRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Criar uma nova mensagem RabbitMQ
    @PostMapping
    public ResponseEntity<RabbitMQMessage> createMessage(@RequestBody RabbitMQMessage message) {
        if (message == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // Salvar a mensagem no banco de dados
        RabbitMQMessage createdMessage = rabbitMQMessageRepository.save(message);

        // Enviar a mensagem para a fila RabbitMQ
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.PAYMENT_ROUTING_KEY, message.getMessageContent());

        return ResponseEntity.status(201).body(createdMessage);
    }

    // Obter todas as mensagens RabbitMQ
    @GetMapping
    public ResponseEntity<List<RabbitMQMessage>> getAllMessages() {
        List<RabbitMQMessage> messages = rabbitMQMessageRepository.findAll();
        return ResponseEntity.ok(messages);
    }

    // Obter mensagem RabbitMQ por ID
    @GetMapping("/{id}")
    public ResponseEntity<RabbitMQMessage> getMessageById(@PathVariable Long id) {
        Optional<RabbitMQMessage> message = rabbitMQMessageRepository.findById(id);
        if (message.isPresent()) {
            return ResponseEntity.ok(message.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Atualizar mensagem RabbitMQ
    @PutMapping("/{id}")
    public ResponseEntity<RabbitMQMessage> updateMessage(@PathVariable Long id, @RequestBody RabbitMQMessage message) {
        if (!rabbitMQMessageRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        message.setId(id);
        RabbitMQMessage updatedMessage = rabbitMQMessageRepository.save(message);
        return ResponseEntity.ok(updatedMessage);
    }

    // Deletar mensagem RabbitMQ
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        if (!rabbitMQMessageRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        rabbitMQMessageRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
