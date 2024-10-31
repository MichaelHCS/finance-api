package com.apifinance.jpa.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apifinance.jpa.dtos.FraudCheckRequest;

import com.apifinance.jpa.exceptions.ResourceNotFoundException;
import com.apifinance.jpa.services.FraudCheckService;

@RestController
@RequestMapping("/fraud-checks")
public class FraudCheckController {

    private static final Logger logger = LoggerFactory.getLogger(FraudCheckController.class);
    private final FraudCheckService fraudCheckService;

    @Autowired
    public FraudCheckController(FraudCheckService fraudCheckService) {
        this.fraudCheckService = fraudCheckService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<Void> analyzePayment(@RequestBody FraudCheckRequest request) {

        logger.info("Iniciando a análise de fraude para o pagamento ID: {}", request.getPaymentId());

        try {
            fraudCheckService.analyzePayment(
                request.getPaymentId(),
                request.getRabbitMqMessageId(),
                request.getFraudStatus(),
                request.getFraudReason()
            );

            logger.info("Análise de fraude concluída com sucesso para o pagamento ID: {}", request.getPaymentId());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            logger.error("Erro ao analisar o pagamento ID: {}: {}", request.getPaymentId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ResourceNotFoundException e) {
            logger.error("Erro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Erro inesperado ao analisar o pagamento ID: {}: {}", request.getPaymentId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
