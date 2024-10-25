package com.apifinance.jpa.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.apifinance.jpa.configrabbitmq.RabbitMqConfig;
import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.models.Customer;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.repositories.CustomerRepository;
import com.apifinance.jpa.repositories.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RabbitMqService rabbitMqService; // Dependência do RabbitMqService
    private final CustomerRepository customerRepository;

    // Injeção de dependências
    public PaymentService(PaymentRepository paymentRepository, 
                          RabbitMqService rabbitMqService, 
                          CustomerRepository customerRepository) {
        this.paymentRepository = paymentRepository;
        this.rabbitMqService = rabbitMqService;
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

        // Publicar a mensagem de pagamento
        publishPaymentMessage(savedPayment);
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

    private void publishPaymentMessage(Payment payment) {
        String messageContent = createPaymentMessage(payment);
        String exchange = RabbitMqConfig.EXCHANGE;
        String routingKey = RabbitMqConfig.ROUTING_KEY;

        rabbitMqService.publishMessage(exchange, routingKey, messageContent);
    }

    // Método para criar a mensagem de pagamento
    private String createPaymentMessage(Payment payment) {
        return "Pagamento criado com ID: " + payment.getId();
    }
}
