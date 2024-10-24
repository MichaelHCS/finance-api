package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.enums.RabbitMqMessageStatus;
import com.apifinance.jpa.models.Customer;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.repositories.CustomerRepository;
import com.apifinance.jpa.repositories.PaymentRepository;
import com.apifinance.jpa.repositories.RabbitMqMessageRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RabbitMqService rabbitMqService;
    private final RabbitMqMessageRepository rabbitmqMessageRepository;
    private final CustomerRepository customerRepository;

    // Injeção de dependências
    public PaymentService(PaymentRepository paymentRepository, 
                          RabbitMqService rabbitMqService, 
                          RabbitMqMessageRepository rabbitmqMessageRepository,
                          CustomerRepository customerRepository) {
        this.paymentRepository = paymentRepository;
        this.rabbitMqService = rabbitMqService;
        this.rabbitmqMessageRepository = rabbitmqMessageRepository;
        this.customerRepository = customerRepository;
    }

    // Método para criar um pagamento associado a um cliente
    public Payment createPayment(Payment payment, Long customerId) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }

        // Verificar se o cliente existe
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));

        // Associar o cliente ao pagamento
        payment.setCustomer(customer);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        Payment savedPayment = paymentRepository.save(payment);

        // Publicar a mensagem de verificação de fraude
        publishFraudCheckMessage(savedPayment);
        return savedPayment;
    }

    // Método para buscar todos os pagamentos
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    // Método para buscar um pagamento por ID
    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    // Método para salvar (atualizar) um pagamento
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    // Método para deletar um pagamento por ID
    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }

    // Método para publicar a mensagem de verificação de fraude
    private void publishFraudCheckMessage(Payment payment) {
        String messageContent = createFraudCheckMessage(payment);
        String exchange = "fraud_check_exchange";
        String routingKey = "fraud.check";
        rabbitMqService.sendMessage(exchange, routingKey, messageContent);

        RabbitMqMessage rabbitMqMessage = new RabbitMqMessage();
        rabbitMqMessage.setMessageContent(messageContent);
        rabbitMqMessage.setStatus(RabbitMqMessageStatus.SENT);
        rabbitMqMessage.setProcessedAt(ZonedDateTime.now());
        rabbitMqMessage.setSentAt(ZonedDateTime.now());
        rabbitmqMessageRepository.save(rabbitMqMessage);
    }

    // Método para criar a mensagem de verificação de fraude
    private String createFraudCheckMessage(Payment payment) {
        return "Verificar fraude para pagamento ID: " + payment.getId();
    }
}
