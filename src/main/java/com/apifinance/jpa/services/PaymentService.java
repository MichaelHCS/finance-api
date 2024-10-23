package com.apifinance.jpa.services;

import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RabbitMqService rabbitMqService;  // Injeta o RabbitMqService para enviar mensagens

    // Lista todos os pagamentos
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    // Busca pagamento por ID
    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    // Cria um novo pagamento e envia uma mensagem para o RabbitMQ
    public Payment createPayment(Payment payment) {

        if (payment.getCustomer() == null) {
            throw new IllegalArgumentException("O cliente não pode ser nulo");
        }
        if (payment.getAmount() == null) {
            throw new IllegalArgumentException("O valor não pode ser nulo");
        }
        if (payment.getCurrency() == null || payment.getCurrency().isEmpty()) {
            throw new IllegalArgumentException("A moeda não pode ser nula ou vazia");
        }
        // Define o status inicial como "Aguardando análise de fraude"
        payment.setStatus(PaymentStatus.PENDING);

        // Define o horário de criação/atualização
        payment.setUpdatedAt(ZonedDateTime.now());

        // Salva o pagamento no banco de dados
        Payment savedPayment = paymentRepository.save(payment);

        // Cria e envia a mensagem para o RabbitMQ
        String messageContent = "Pagamento criado com ID: " + savedPayment.getId();
        rabbitMqService.sendMessage("exchange.fraude", "routingKey.fraude", messageContent);

        return savedPayment;
    }

    // Salva ou atualiza um pagamento
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    // Deleta pagamento por ID
    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }
}
