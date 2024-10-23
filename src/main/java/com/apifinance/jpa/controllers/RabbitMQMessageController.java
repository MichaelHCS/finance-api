package com.apifinance.jpa.controllers;

import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.services.RabbitMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rabbitmq-messages")
public class RabbitMqMessageController {

    @Autowired
    private RabbitMqService rabbitMqMessageService;

    @GetMapping
    public List<RabbitMqMessage> getAllRabbitMqMessages() {
        return rabbitMqMessageService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RabbitMqMessage> getRabbitMqMessageById(@PathVariable Long id) {
        RabbitMqMessage rabbitMqMessage = rabbitMqMessageService.findById(id);
        return rabbitMqMessage != null ? ResponseEntity.ok(rabbitMqMessage) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public RabbitMqMessage createRabbitMqMessage(@RequestBody RabbitMqMessage rabbitMqMessage) {
        return rabbitMqMessageService.save(rabbitMqMessage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RabbitMqMessage> updateRabbitMqMessage(@PathVariable Long id, @RequestBody RabbitMqMessage rabbitMqMessage) {
        rabbitMqMessage.setId(id);
        RabbitMqMessage updatedRabbitMqMessage = rabbitMqMessageService.save(rabbitMqMessage);
        return ResponseEntity.ok(updatedRabbitMqMessage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRabbitMqMessage(@PathVariable Long id) {
        rabbitMqMessageService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}