package com.apifinance.jpa.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<Void> processFraudAnalysis(@RequestBody FraudCheckRequest request) {
        logger.info("Iniciando análise de fraude para o pagamento ID: {}", request.getPaymentId());
        try {
            fraudCheckService.processAnalysis(
                    request.getPaymentId(),
                    request.getRabbitMqMessageId(),
                    request.getFraudStatus(),
                    request.getFraudReason()
            );
            logger.info("Análise de fraude concluída para o pagamento ID: {}", request.getPaymentId());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            logger.error("Erro ao analisar pagamento ID: {}: {}", request.getPaymentId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ResourceNotFoundException e) {
            logger.error("Erro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Erro inesperado ao analisar pagamento ID: {}: {}", request.getPaymentId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{fraudCheckId}")
    public ResponseEntity<FraudCheckResponse> fetchFraudCheckById(@PathVariable UUID fraudCheckId) {
        logger.info("Buscando verificação de fraude com ID: {}", fraudCheckId);
        try {
            FraudCheckResponse response = fraudCheckService.fetchById(fraudCheckId);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            logger.error("Erro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Erro inesperado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<FraudCheckResponse>> fetchAllFraudChecks() {
        logger.info("Buscando todas as verificações de fraude");
        List<FraudCheckResponse> fraudChecks = fraudCheckService.fetchAll();
        return ResponseEntity.ok(fraudChecks);
    }

    @DeleteMapping("/{fraudCheckId}")
    public ResponseEntity<String> removeFraudCheck(@PathVariable UUID fraudCheckId) {
        logger.info("Solicitação recebida para deletar verificação de fraude com ID: {}", fraudCheckId);
        try {
            fraudCheckService.deleteById(fraudCheckId);
            logger.info("Verificação de fraude com ID {} deletada com sucesso", fraudCheckId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Verificação de fraude deletada com sucesso.");
        } catch (ResourceNotFoundException e) {
            logger.error("Erro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verificação de fraude não encontrada com o ID: " + fraudCheckId);
        } catch (Exception e) {
            logger.error("Erro inesperado ao deletar verificação de fraude: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar verificação de fraude: " + e.getMessage());
        }
    }
}
