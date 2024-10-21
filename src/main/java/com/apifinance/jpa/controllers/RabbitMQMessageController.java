package com.apifinance.jpa.controllers;

import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.services.RabbitMqMessageService; // Supondo que você tenha um RabbitMqMessageService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rabbitmq-messages")
public class RabbitMqMessageController {

    private final RabbitMqMessageService rabbitMqMessageService;

    @Autowired
    public RabbitMqMessageController(RabbitMqMessageService rabbitMqMessageService) {
        this.rabbitMqMessageService = rabbitMqMessageService;
    }

    @GetMapping
    public ResponseEntity<List<RabbitMqMessage>> getAllMessages() {
        List<RabbitMqMessage> messages = rabbitMqMessageService.findAll();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RabbitMqMessage> getMessageById(@PathVariable Long id) {
        RabbitMqMessage message = rabbitMqMessageService.findById(id);
        return message != null ? ResponseEntity.ok(message) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<RabbitMqMessage> createMessage(@RequestBody RabbitMqMessage rabbitMqMessage) {
        RabbitMqMessage createdMessage = rabbitMqMessageService.save(rabbitMqMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RabbitMqMessage> updateMessage(@PathVariable Long id, @RequestBody RabbitMqMessage rabbitMqMessage) {
        RabbitMqMessage updatedMessage = rabbitMqMessageService.update(id, rabbitMqMessage);
        return updatedMessage != null ? ResponseEntity.ok(updatedMessage) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        return rabbitMqMessageService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
