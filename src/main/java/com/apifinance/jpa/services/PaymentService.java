package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.enums.FraudCheckStatus;
import com.apifinance.jpa.enums.PaymentMethodDetails;
import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.enums.PaymentType;
import com.apifinance.jpa.enums.RabbitMqMessageStatus;
import com.apifinance.jpa.enums.TransactionAction;
import com.apifinance.jpa.models.Customer;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.models.PaymentMethod;
import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.models.TransactionLog;
import com.apifinance.jpa.rabbitmqconfig.RabbitConfig;
import com.apifinance.jpa.repositories.CustomerRepository;
import com.apifinance.jpa.repositories.PaymentMethodRepository;
import com.apifinance.jpa.repositories.PaymentRepository;
//import com.apifinance.jpa.repositories.RabbitMqMessageRepository;
import com.apifinance.jpa.repositories.TransactionLogRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
   // private final RabbitMqMessageRepository rabbitMqMessageRepository; // Incluído
    private final TransactionLogRepository transactionLogRepository;
    private final RabbitTemplate rabbitTemplate;
    private final PaymentMethodRepository paymentMethodRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          //RabbitMqMessageRepository rabbitMqMessageRepository,
                          TransactionLogRepository transactionLogRepository,
                          RabbitTemplate rabbitTemplate,
                          PaymentMethodRepository paymentMethodRepository,
                          CustomerRepository customerRepository) {
        this.paymentRepository = paymentRepository;
        //this.rabbitMqMessageRepository = rabbitMqMessageRepository; // Inicializado
        this.transactionLogRepository = transactionLogRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.paymentMethodRepository = paymentMethodRepository;
        this.customerRepository = customerRepository;
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public Payment createPayment(Payment payment) {
        // Verifique se o paymentMethod não é nulo
        if (payment.getPaymentMethod() == null) {
            throw new IllegalArgumentException("Payment method must not be null.");
        }

        // Buscar o cliente pelo ID
        Customer customer = customerRepository.findById(payment.getCustomer().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

        // Criar o pagamento
        Payment newPayment = new Payment();
        newPayment.setCustomer(customer);
        newPayment.setAmount(payment.getAmount());
        newPayment.setCurrency(payment.getCurrency());
        newPayment.setPaymentMethod(payment.getPaymentMethod());
        newPayment.setPaymentStatus(PaymentStatus.PENDING); // Status inicial
        newPayment.setCreatedAt(ZonedDateTime.now());
        newPayment.setUpdatedAt(null);
        //newPayment.setFraudCheck(payment.getFraudCheck()); // Atualizado para usar a entidade FraudCheck

        // Chame o método createPaymentMethod apenas se o paymentMethod for válido
        createPaymentMethod(payment.getPaymentMethod(), PaymentMethodDetails.BANK);

        // Salve o pagamento
        Payment savedPayment = paymentRepository.save(newPayment);

        // Criação do log de transação
        createTransactionLog(savedPayment, TransactionAction.PAYMENT_CREATED, "Pagamento criado com sucesso.");

        // Criação e envio da mensagem RabbitMQ
        String messageContent = createMessageContent(savedPayment);
        publishToRabbitMq(messageContent);

        // Criar e salvar a mensagem do RabbitMQ
        RabbitMqMessage rabbitMqMessage = new RabbitMqMessage();
        rabbitMqMessage.setMessageContent(messageContent);
        rabbitMqMessage.setStatus(RabbitMqMessageStatus.SENT);
        rabbitMqMessage.setSentAt(ZonedDateTime.now());
        //rabbitMqMessageRepository.save(rabbitMqMessage); // Salvar a mensagem

        return savedPayment;
    }

    public Payment update(Long id, Payment payment) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado."));

        // Atualiza o campo fraud_check_id e o status do pagamento com base na verificação de fraude
        if (payment.getFraudCheck() != null) {

            FraudCheckStatus fraudStatus = payment.getFraudCheck().getFraudStatus();

            if (fraudStatus == FraudCheckStatus.APPROVED) {
                existingPayment.setPaymentStatus(PaymentStatus.APPROVED);
            } else if (fraudStatus == FraudCheckStatus.REJECTED) {
                existingPayment.setPaymentStatus(PaymentStatus.REJECTED);
                // Se houver erro ou necessidade de revisão, armazena o motivo
                if (payment.getFraudCheck().getCheckReason() != null) {
                    existingPayment.setCheckReason(payment.getFraudCheck().getCheckReason()); // Armazena o motivo de revisão
                }
            }
            //existingPayment.setFraudCheck(payment.getFraudCheck()); // Atualiza o fraud_check associado
            // Atualiza o campo updated_at
            //existingPayment.setUpdatedAt(ZonedDateTime.now());
        } else {
            // Mantém updated_at como null se o fraud_check não for atualizado
            //existingPayment.setUpdatedAt(null);
        }

        return paymentRepository.save(existingPayment);
    }

    public boolean delete(Long id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void createTransactionLog(Payment payment, TransactionAction action, String details) {
        TransactionLog transactionLog = new TransactionLog(payment, action, ZonedDateTime.now(), details);
        transactionLogRepository.save(transactionLog);
    }

    private void createPaymentMethod(PaymentType type, PaymentMethodDetails details) {
        PaymentMethod existingMethod = paymentMethodRepository.findByTypeAndDetails(type, details);
        if (existingMethod == null) {
            // Se não existir, crie um novo
            PaymentMethod paymentMethod = new PaymentMethod(type, details);
            paymentMethodRepository.save(paymentMethod);
        } else {
            // Se já existir, apenas associá-lo ao pagamento
            System.out.println("Método de pagamento já existe e será utilizado.");
        }
    }

    private String createMessageContent(Payment payment) {
        return String.format("Pagamento criado: %s, Método: %s, Valor: %.2f, Status: %s",
                payment.getId(), payment.getPaymentMethod(), payment.getAmount(), payment.getStatus());
    }

    private void publishToRabbitMq(String messageContent) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.PAYMENT_ROUTING_KEY, messageContent);
    }
}
