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

    private final RabbitMqService rabbitMqService;

    @Autowired
    public RabbitMqMessageController(RabbitMqService rabbitMqService) {
        this.rabbitMqService = rabbitMqService;
    }

    // Endpoint para obter todas as mensagens
    @GetMapping
    public List<RabbitMqMessage> getAllRabbitMqMessages() {
        return rabbitMqService.findAll();
    }

    // Endpoint para obter uma mensagem por ID
    @GetMapping("/{id}")
    public ResponseEntity<RabbitMqMessage> getRabbitMqMessageById(@PathVariable Long id) {
        RabbitMqMessage rabbitMqMessage = rabbitMqService.findById(id);
        return rabbitMqMessage != null ? ResponseEntity.ok(rabbitMqMessage) : ResponseEntity.notFound().build();
    }

    // Endpoint para publicar uma nova mensagem e salvar no repositório
    @PostMapping
    public ResponseEntity<RabbitMqMessage> createRabbitMqMessage(@RequestParam String exchange, 
                                                                  @RequestParam String routingKey, 
                                                                  @RequestBody String messageContent) {
        // Publicar a mensagem
        rabbitMqService.publishMessage(exchange, routingKey, messageContent);
        
        // Retornar uma resposta indicando que a mensagem foi publicada
        return ResponseEntity.ok().build();
    }

    // Endpoint para deletar uma mensagem por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRabbitMqMessage(@PathVariable Long id) {
        rabbitMqService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
