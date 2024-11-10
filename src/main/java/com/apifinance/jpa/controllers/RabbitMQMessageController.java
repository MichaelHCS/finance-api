package com.apifinance.jpa.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.services.RabbitMqService;

@RestController
@RequestMapping("/rabbitmq-messages")
public class RabbitMqMessageController {

    private final RabbitMqService rabbitMqService;

    @Autowired
    public RabbitMqMessageController(RabbitMqService rabbitMqService) {
        this.rabbitMqService = rabbitMqService;
    }

    @PostMapping
    public ResponseEntity<String> createRabbitMqMessage(@RequestParam String messageContent) {
        try {
            rabbitMqService.publishMessage(messageContent);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Mensagem enviada para o RabbitMQ com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao enviar mensagem para o RabbitMQ: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<RabbitMqMessage>> getAllMessages() {
        try {
            List<RabbitMqMessage> messages = rabbitMqService.getAllMessages();
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMessageById(@PathVariable UUID id) {
        try {
            RabbitMqMessage message = rabbitMqService.getMessageById(id);
            if (message != null) {
                return ResponseEntity.ok(message);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Mensagem não encontrada com ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar mensagem: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RabbitMqMessage> updateRabbitMqMessage(
            @PathVariable UUID id,
            @RequestBody RabbitMqMessage updatedMessage) {
        try {
            RabbitMqMessage message = rabbitMqService.updateMessage(id, updatedMessage);
            return ResponseEntity.ok(message); // Return the updated message
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
