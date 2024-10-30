package com.apifinance.jpa.controllers;

import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.services.RabbitMqService;
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
    public ResponseEntity<RabbitMqMessage> createRabbitMqMessage(
        @RequestParam String exchange, 
        @RequestParam String routingKey, 
        @RequestBody String messageContent) {
        
        rabbitMqService.publishMessage(exchange, routingKey, messageContent);
        return ResponseEntity.ok().build();
    }
}
