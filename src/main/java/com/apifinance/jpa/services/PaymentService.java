package com.apifinance.jpa.services;

import com.apifinance.jpa.configrabbitmq.RabbitMqConfig;
import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.enums.RabbitMqMessageStatus;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.repositories.PaymentRepository;
import com.apifinance.jpa.repositories.RabbitMqMessageRepository;
import com.apifinance.jpa.repositories.TransactionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RabbitMqMessageRepository rabbitMqMessageRepository;
    private final RabbitMqService rabbitMqService;
    //private final TransactionLogRepository transactionLogRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          RabbitMqMessageRepository rabbitMqMessageRepository,
                          RabbitMqService rabbitMqService,
                          TransactionLogRepository transactionLogRepository) {
        this.paymentRepository = paymentRepository;
        this.rabbitMqMessageRepository = rabbitMqMessageRepository;
        this.rabbitMqService = rabbitMqService;
        //this.transactionLogRepository = transactionLogRepository;
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public void createdPayment(Payment payment) {
        
        payment.setCreatedAt(ZonedDateTime.now());
        payment.setPaymentStatus(PaymentStatus.PENDING); 
        
        paymentRepository.save(payment);
        
        String messageContent = createPaymentMessage(payment);
    
        rabbitMqService.sendMessage(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_KEY, messageContent);
        
        RabbitMqMessage rabbitMqMessage = new RabbitMqMessage();
        rabbitMqMessage.setMessageContent(messageContent);
        rabbitMqMessage.setStatus(RabbitMqMessageStatus.SENT); // Enum correto
        rabbitMqMessage.setSentAt(ZonedDateTime.now());
        //rabbitMqMessage.setPayments(payments); // Relaciona o pagamento com a mensagem
    
        // 6. Salva o registro da mensagem no banco de dados
        rabbitMqMessageRepository.save(rabbitMqMessage);
    }

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }

    // Método auxiliar para criar o conteúdo da mensagem de pagamento
    private String createPaymentMessage(Payment payment) {
        // Aqui você pode implementar a lógica para converter o objeto Payment em uma string JSON, XML ou outro formato de mensagem
        return "Pagamento ID: " + payment.getId() + " - Status: " + payment.getPaymentStatus();
    }
}
