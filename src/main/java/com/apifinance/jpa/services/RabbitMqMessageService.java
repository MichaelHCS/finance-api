package com.apifinance.jpa.services;

import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.repositories.RabbitMqMessageRepository; // Supondo que você tenha um RabbitMqMessageRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RabbitMqMessageService {

    private final RabbitMqMessageRepository rabbitMqMessageRepository;

    @Autowired
    public RabbitMqMessageService(RabbitMqMessageRepository rabbitMqMessageRepository) {
        this.rabbitMqMessageRepository = rabbitMqMessageRepository;
    }

    public List<RabbitMqMessage> findAll() {
        return rabbitMqMessageRepository.findAll();
    }

    public RabbitMqMessage findById(Long id) {
        Optional<RabbitMqMessage> optionalMessage = rabbitMqMessageRepository.findById(id);
        return optionalMessage.orElse(null); // Retorna null se não encontrar
    }

    public RabbitMqMessage save(RabbitMqMessage rabbitMqMessage) {
        return rabbitMqMessageRepository.save(rabbitMqMessage);
    }

    public RabbitMqMessage update(Long id, RabbitMqMessage rabbitMqMessage) {
        if (rabbitMqMessageRepository.existsById(id)) {
            rabbitMqMessage.setId(id); // Define o ID da mensagem a ser atualizada
            return rabbitMqMessageRepository.save(rabbitMqMessage);
        }
        return null; // Retorna null se a mensagem não existir
    }

    public boolean delete(Long id) {
        if (rabbitMqMessageRepository.existsById(id)) {
            rabbitMqMessageRepository.deleteById(id);
            return true;
        }
        return false; // Retorna false se a mensagem não existir
    }
}
