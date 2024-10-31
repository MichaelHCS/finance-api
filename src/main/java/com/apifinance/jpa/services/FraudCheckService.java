package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckStatus;
import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.repositories.FraudCheckRepository;
import com.apifinance.jpa.repositories.PaymentRepository;
import com.apifinance.jpa.repositories.RabbitMqMessageRepository;
import com.apifinance.jpa.exceptions.ResourceNotFoundException;

@Service
public class FraudCheckService {

    private static final Logger logger = LoggerFactory.getLogger(FraudCheckService.class);

    @Autowired
    private FraudCheckRepository fraudCheckRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RabbitMqMessageRepository rabbitMqMessageRepository;

    public void analyzePayment(Long paymentId, Long rabbitMqMessageId,
            FraudCheckStatus fraudStatus, FraudCheckReason fraudReason) {
        logger.info("Iniciando análise de fraude para o pagamento ID: {}", paymentId);

        // Verifica se o pagamento existe
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado: " + paymentId));

        // Verifica se a mensagem RabbitMQ existe
        RabbitMqMessage rabbitMqMessage = rabbitMqMessageRepository.findById(rabbitMqMessageId)
                .orElseThrow(() -> new ResourceNotFoundException("Mensagem RabbitMQ não encontrada: " + rabbitMqMessageId));

        // Criação do objeto FraudCheck
        FraudCheck fraudCheck = new FraudCheck();
        fraudCheck.setPayment(payment);
        fraudCheck.setRabbitMqMessage(rabbitMqMessage);
        fraudCheck.setFraudStatus(fraudStatus);
        fraudCheck.setFraudReason(fraudReason);
        fraudCheck.setCheckedAt(ZonedDateTime.now());

        // Criar um mapa para associar FraudCheckStatus a ações
        Map<FraudCheckStatus, Consumer<Payment>> statusActions = new HashMap<>();

        statusActions.put(FraudCheckStatus.REJECTED, p -> {
            logger.warn("Pagamento ID: {} foi rejeitado. Motivo: {}", p.getId(), fraudReason.getDescription());
        });

        statusActions.put(FraudCheckStatus.APPROVED, p -> {
            logger.info("Pagamento ID: {} foi aprovado.", p.getId());
        });

        // Executar a ação correspondente ao status
        Consumer<Payment> action = statusActions.get(fraudStatus);
        if (action != null) {
            action.accept(payment);
        } else {
            logger.error("Status de fraude inválido para o pagamento ID: {}.", payment.getId());
            throw new IllegalArgumentException("Status de fraude inválido.");
        }

        // Salva a verificação de fraude
        fraudCheckRepository.save(fraudCheck);
        logger.info("Verificação de fraude salva com sucesso para o pagamento ID: {}.", payment.getId());

        // Atualiza o status do pagamento
        payment.setPaymentStatus(fraudCheck.getFraudStatus().toPaymentStatus());
        payment.setUpdatedAt(ZonedDateTime.now());

        payment.setFraudCheck(fraudCheck); // Isso depende da implementação da classe Payment
        // Salva o pagamento atualizado
        paymentRepository.save(payment);
        logger.info("Status do pagamento ID: {} atualizado para: {}", payment.getId(), fraudCheck.getFraudStatus());
    }
}
