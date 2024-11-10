package com.apifinance.jpa.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apifinance.jpa.dtos.FraudCheckRequest;
import com.apifinance.jpa.dtos.FraudCheckResponse;
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
    public ResponseEntity<String> processFraudAnalysis(@RequestBody FraudCheckRequest request) {
        logger.info("Iniciando análise de fraude para o pagamento ID: {}", request.getPaymentId());
        try {
            fraudCheckService.processAnalysis(
                    request.getPaymentId(),
                    request.getRabbitMqMessageId(),
                    request.getFraudStatus(),
                    request.getFraudReason()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body("Análise de fraude concluída");
        } catch (IllegalArgumentException e) {
            logger.error("Erro ao analisar pagamento ID: {}: {}", request.getPaymentId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            logger.error("Erro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erro inesperado ao analisar pagamento ID: {}: {}", request.getPaymentId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado");
        }
    }

    @GetMapping("/{fraudCheckId}")
    public ResponseEntity<?> fetchFraudCheckById(@PathVariable UUID fraudCheckId) {
        logger.info("Buscando verificação de fraude com ID: {}", fraudCheckId);
        try {
            FraudCheckResponse response = fraudCheckService.fetchById(fraudCheckId);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            logger.error("Erro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erro inesperado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado");
        }
    }

    @GetMapping
    public ResponseEntity<?> fetchAllFraudChecks() {
        logger.info("Buscando todas as verificações de fraude");
        List<FraudCheckResponse> fraudChecks = fraudCheckService.fetchAll();

        if (fraudChecks.isEmpty()) {
            logger.warn("Nenhuma verificação de fraude encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma verificação de fraude encontrada");
        }

        return ResponseEntity.ok(fraudChecks);
    }

    @PutMapping("/{fraudCheckId}")
    public ResponseEntity<?> updateFraudCheck(
            @PathVariable UUID fraudCheckId,
            @RequestBody FraudCheckRequest request) {
        logger.info("Atualizando verificação de fraude com ID: {}", fraudCheckId);
        try {
            FraudCheckResponse updatedFraudCheck = fraudCheckService.updateFraudCheck(fraudCheckId, request);
            logger.info("Verificação de fraude atualizada com sucesso para o ID: {}", fraudCheckId);
            return ResponseEntity.ok(updatedFraudCheck);
        } catch (ResourceNotFoundException e) {
            logger.error("Erro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Erro ao atualizar verificação de fraude: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erro inesperado ao atualizar verificação de fraude: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado");
        }
    }
}
