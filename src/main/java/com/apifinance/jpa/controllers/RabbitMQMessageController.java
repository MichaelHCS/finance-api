package com.apifinance.jpa.controllers;

import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.services.RabbitMqService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rabbitmq-messages")
public class RabbitMqMessageController {

    private final RabbitMqService rabbitMqService;

    @Autowired
    public RabbitMqMessageController(RabbitMqService rabbitMqService) {
        this.rabbitMqService = rabbitMqService;
    }

    @PostMapping
    public ResponseEntity<Void> createRabbitMqMessage(
            @RequestParam String messageContent) {

        rabbitMqService.publishMessage(messageContent);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<RabbitMqMessage>> getAllMessages() {
        List<RabbitMqMessage> messages = rabbitMqService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RabbitMqMessage> getMessageById(@PathVariable UUID id) {
        RabbitMqMessage message = rabbitMqService.getMessageById(id);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RabbitMqMessage> updateRabbitMqMessage(
            @PathVariable UUID id,
            @RequestBody RabbitMqMessage updatedMessage) {
        RabbitMqMessage message = rabbitMqService.updateMessage(id, updatedMessage);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRabbitMqMessage(@PathVariable UUID id) {
        rabbitMqService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
