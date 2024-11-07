package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.dtos.FraudCheckRequest;
import com.apifinance.jpa.dtos.FraudCheckResponse;
import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckStatus;
import com.apifinance.jpa.exceptions.ResourceNotFoundException;
import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.repositories.FraudCheckRepository;
import com.apifinance.jpa.repositories.PaymentRepository;
import com.apifinance.jpa.repositories.RabbitMqMessageRepository;

@Service
public class FraudCheckService {

    private static final Logger logger = LoggerFactory.getLogger(FraudCheckService.class);

    @Autowired
    private FraudCheckRepository fraudCheckRepository;

    @Autowired
    private TransactionLogService transactionLogService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RabbitMqMessageRepository rabbitMqMessageRepository;

    public void processAnalysis(UUID paymentId, UUID rabbitMqMessageId,
            FraudCheckStatus fraudStatus, FraudCheckReason fraudReason) {

        logger.info("Iniciando análise de fraude para o pagamento ID: {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado: " + paymentId));

        RabbitMqMessage rabbitMqMessage = rabbitMqMessageRepository.findById(rabbitMqMessageId)
                .orElseThrow(() -> new ResourceNotFoundException("Mensagem RabbitMQ não encontrada: " + rabbitMqMessageId));

        FraudCheck fraudCheck = new FraudCheck();
        fraudCheck.setPayment(payment);
        fraudCheck.setRabbitMqMessage(rabbitMqMessage);
        fraudCheck.setFraudStatus(fraudStatus);
        fraudCheck.setFraudReason(fraudReason);
        fraudCheck.setCheckedAt(ZonedDateTime.now());

        Map<FraudCheckStatus, Consumer<Payment>> statusActions = new EnumMap<>(FraudCheckStatus.class);

        statusActions.put(FraudCheckStatus.REJECTED, p -> {
            logger.warn("Pagamento ID: {} foi rejeitado. Motivo: {}", p.getId(), fraudReason.getDescription());
            transactionLogService.logFraudDetected(p, fraudReason);
        });

        statusActions.put(FraudCheckStatus.APPROVED, p -> {
            logger.info("Pagamento ID: {} foi aprovado.", p.getId());
            transactionLogService.logPaymentCreated(p);
        });

        Consumer<Payment> action = statusActions.get(fraudStatus);
        if (action != null) {
            action.accept(payment);
        } else {
            logger.error("Status de fraude inválido para o pagamento ID: {}.", payment.getId());
            throw new IllegalArgumentException("Status de fraude inválido.");
        }

        fraudCheckRepository.save(fraudCheck);
        logger.info("Verificação de fraude salva com sucesso para o pagamento ID: {}.", payment.getId());

        payment.setPaymentStatus(fraudCheck.getFraudStatus().toPaymentStatus());
        payment.setUpdatedAt(ZonedDateTime.now());
        payment.setFraudCheck(fraudCheck);

        paymentRepository.save(payment);
        logger.info("Status do pagamento atualizado para: {}", fraudCheck.getFraudStatus());
    }

    public FraudCheckResponse fetchById(UUID fraudCheckId) {
        FraudCheck fraudCheck = fraudCheckRepository.findById(fraudCheckId)
                .orElseThrow(() -> new ResourceNotFoundException("Verificação de fraude não encontrada para o ID: " + fraudCheckId));

        return convertToResponse(fraudCheck);
    }

    public List<FraudCheckResponse> fetchAll() {
        return fraudCheckRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public FraudCheckResponse updateFraudCheck(UUID fraudCheckId, FraudCheckRequest updateRequest) {
        FraudCheck existingFraudCheck = fraudCheckRepository.findById(fraudCheckId)
                .orElseThrow(() -> new ResourceNotFoundException("Verificação de fraude não encontrada para o ID: " + fraudCheckId));

        // Atualiza os campos permitidos e o horário de verificação
        ZonedDateTime now = ZonedDateTime.now();
        existingFraudCheck.setCheckedAt(now);

        if (updateRequest.getFraudStatus() != null) {
            existingFraudCheck.setFraudStatus(updateRequest.getFraudStatus());

            // Atualiza o status e o horário no Payment associado
            Payment payment = existingFraudCheck.getPayment();
            payment.setPaymentStatus(updateRequest.getFraudStatus().toPaymentStatus());
            payment.setUpdatedAt(now);

            paymentRepository.save(payment);
            logger.info("Status do pagamento ID: {} atualizado para: {}. Horário: {}", payment.getId(), updateRequest.getFraudStatus(), now);
        }

        if (updateRequest.getFraudReason() != null) {
            existingFraudCheck.setFraudReason(updateRequest.getFraudReason());
        }
        if (updateRequest.getRabbitMqMessageId() != null) {
            RabbitMqMessage rabbitMqMessage = rabbitMqMessageRepository.findById(updateRequest.getRabbitMqMessageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mensagem RabbitMQ não encontrada com o ID: " + updateRequest.getRabbitMqMessageId()));
            existingFraudCheck.setRabbitMqMessage(rabbitMqMessage);
        }

        // Salva as atualizações e log de tempo
        fraudCheckRepository.save(existingFraudCheck);
        logger.info("Verificação de fraude com ID: {} atualizada com sucesso. Horário: {}", fraudCheckId, now);

        return convertToResponse(existingFraudCheck);
    }

    private FraudCheckResponse convertToResponse(FraudCheck fraudCheck) {
        return new FraudCheckResponse(
                fraudCheck.getId(),
                fraudCheck.getPayment().getId(),
                fraudCheck.getRabbitMqMessage().getId(),
                fraudCheck.getFraudStatus(),
                fraudCheck.getFraudReason(),
                fraudCheck.getCheckedAt()
        );
    }

    public void deleteById(UUID fraudCheckId) {
        // Buscando o FraudCheck para garantir que ele existe
        FraudCheck fraudCheck = fraudCheckRepository.findById(fraudCheckId)
                .orElseThrow(() -> new ResourceNotFoundException("Verificação de fraude não encontrada com o ID: " + fraudCheckId));

        // Removendo a associação com Payment
        Payment payment = fraudCheck.getPayment();
        if (payment != null) {
            payment.setFraudCheck(null);  // Remover a associação de FraudCheck do Payment
            paymentRepository.save(payment);  // Salvando o Payment sem a associação com FraudCheck
            logger.info("Associação de fraude removida do pagamento ID: {}", payment.getId());
        }

        // Agora excluímos o FraudCheck
        fraudCheckRepository.delete(fraudCheck);
        logger.info("Verificação de fraude com ID: {} excluída com sucesso.", fraudCheckId);
    }

}
