package com.apifinance.jpa.services;

import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.repositories.RabbitMqMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitMqMessageService {

    @Autowired
    private RabbitMqMessageRepository rabbitMqMessageRepository;

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
