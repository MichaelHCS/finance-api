package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // Importando a classe TransactionLog
import org.springframework.transaction.annotation.Transactional; // Importando a classe PaymentMethod

import com.apifinance.jpa.enums.PaymentMethodDetailsType;
import com.apifinance.jpa.enums.PaymentMethodType;
import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.enums.TransactionAction;
import com.apifinance.jpa.enums.rabbitmqMessageStatus; // Importando o repositório TransactionLog
import com.apifinance.jpa.exceptions.PaymentNotFoundException; // Importando o repositório PaymentMethod
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.models.PaymentMethod;
import com.apifinance.jpa.models.RabbitMQMessage;
import com.apifinance.jpa.models.TransactionLog;
import com.apifinance.jpa.rabbitmqConfig.RabbitConfig;
import com.apifinance.jpa.repositories.PaymentMethodRepository;
import com.apifinance.jpa.repositories.PaymentRepository;
import com.apifinance.jpa.repositories.RabbitMQMessageRepository;
import com.apifinance.jpa.repositories.TransactionLogRepository;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQMessageRepository rabbitmqMessageRepository;
    private final TransactionLogRepository transactionLogRepository; // Adicionando o repositório TransactionLog
    private final PaymentMethodRepository paymentMethodRepository; // Adicionando o repositório PaymentMethod
    private ZonedDateTime processedAt;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, RabbitTemplate rabbitTemplate,
                          RabbitMQMessageRepository rabbitmqMessageRepository,
                          TransactionLogRepository transactionLogRepository,
                          PaymentMethodRepository paymentMethodRepository) {
        this.paymentRepository = paymentRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitmqMessageRepository = rabbitmqMessageRepository;
        this.transactionLogRepository = transactionLogRepository; // Inicializando o repositório de logs
        this.paymentMethodRepository = paymentMethodRepository; // Inicializando o repositório de métodos de pagamento
    }

    @Transactional
    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + paymentId));
        
        paymentRepository.delete(payment);
        logger.info("Pagamento excluído com sucesso: " + payment);
    }

    // Criar pagamento com status "Aguardando análise de fraude" e enviar mensagem RabbitMQ
    @Transactional
    public Payment createPayment(Payment payment) {
        // Definir status inicial do pagamento
        payment.setStatus(PaymentStatus.PENDING); // Ajustar para o status desejado

        // Criar registro na tabela rabbitmq_message
        RabbitMQMessage rabbitMQMessage = new RabbitMQMessage();
        rabbitMQMessage.setMessageContent("Payment Created");
        rabbitMQMessage.setStatus(rabbitmqMessageStatus.PENDING);
        rabbitMQMessage.setSentAt(ZonedDateTime.now());
        rabbitMQMessage.setProcessedAt(processedAt);
        RabbitMQMessage savedMessage = rabbitmqMessageRepository.save(rabbitMQMessage);

        payment.setRabbitMQMessage(savedMessage);  // Certifique-se de que há um campo para essa associação no Payment
        
        
        Payment savedPayment = paymentRepository.save(payment); // Salve novamente o pagamento com a associação

        // Criar log de transação
        TransactionLog transactionLog = new TransactionLog(); // Instanciando o log de transação
        transactionLog.setPayment(savedPayment); // Associar o pagamento ao log
        transactionLog.setAction(TransactionAction.PAYMENT_CREATED); // Ação a ser registrada
        transactionLog.setTimestamp(ZonedDateTime.now()); // Usando a data e hora atual
        transactionLog.setDetails(PaymentMethodDetailsType.BANK.name()); // Adicionando detalhes
        transactionLogRepository.save(transactionLog); // Salvando o log de transação

        // Enviar mensagem para RabbitMQ
        sendRabbitMQMessage(savedMessage);

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setType(PaymentMethodType.CREDIT_CARD); // Defina o tipo de pagamento
        paymentMethod.setDetails(PaymentMethodDetailsType.BANK.name());
        paymentMethod.setPayment(savedPayment);

        paymentMethodRepository.save(paymentMethod);

        return savedPayment; // Retorna o pagamento salvo
    }

    // Método para enviar mensagens para o RabbitMQ com tratamento de erros e tentativa de reenvio
    private void sendRabbitMQMessage(RabbitMQMessage rabbitMQMessage) {
        try {
            // Enviar a mensagem para RabbitMQ
            String messageText = "Payment Created"; // Texto simples
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.PAYMENT_ROUTING_KEY, messageText);
            logger.info("Message sent to RabbitMQ: {}", messageText);

            // Atualizar o status para enviado após sucesso
            rabbitMQMessage.setStatus(rabbitmqMessageStatus.SENT);
            rabbitMQMessage.setProcessedAt(ZonedDateTime.now());
            rabbitmqMessageRepository.save(rabbitMQMessage);
        } catch (AmqpException e) {
            logger.error("Error sending message to RabbitMQ: {}", e.getMessage());

            // Atualizar o status para erro em caso de falha
            rabbitMQMessage.setStatus(rabbitmqMessageStatus.ERROR);
            rabbitMQMessage.setProcessedAt(ZonedDateTime.now());
            rabbitmqMessageRepository.save(rabbitMQMessage);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
        }
    }

    // Buscar pagamento por ID
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + id));
    }

    // Listar todos os pagamentos
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Atualizar pagamento
    @Transactional
    public Payment updatePayment(Long id, Payment payment) {
        Payment existingPayment = getPaymentById(id);
        existingPayment.setAmount(payment.getAmount());
        existingPayment.setStatus(payment.getStatus());
        return paymentRepository.save(existingPayment);
    }

    
}
