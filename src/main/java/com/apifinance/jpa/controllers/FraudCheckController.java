package com.apifinance.jpa.controllers;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.models.Payment; // Importando a classe Payment
import com.apifinance.jpa.repositories.FraudCheckRepository;
import com.apifinance.jpa.repositories.PaymentRepository;
import com.apifinance.jpa.requests.FraudCheckRequest; // Importando o repositório Payment

import jakarta.validation.Valid;

@RestController
@RequestMapping("/fraud-check")
public class FraudCheckController {

    @Autowired
    private FraudCheckRepository fraudCheckRepository;

    @Autowired
    private PaymentRepository paymentRepository; // Supondo que você tenha esse repositório

    // Criar uma nova verificação de fraude
    @PostMapping
    public ResponseEntity<FraudCheck> createFraudCheck(@Valid @RequestBody FraudCheckRequest request) {
    // Cria uma nova verificação de fraude com os dados recebidos
        Optional<Payment> payment = paymentRepository.findById(request.getPaymentId());
        if (payment.isPresent()) {
            FraudCheck fraudCheck = new FraudCheck(
                payment.get(),
                request.getFraudStatus(),
                request.getCheckReason(),
                request.getRabbitmqMessageId() // ID da mensagem RabbitMQ
            );
            FraudCheck createdFraudCheck = fraudCheckRepository.save(fraudCheck);
            return ResponseEntity.status(201).body(createdFraudCheck);
        } else {
            return ResponseEntity.badRequest().body(null); // Ou trate o erro de forma apropriada
    }
}

    // Obter todas as verificações de fraude
    @GetMapping
    public ResponseEntity<List<FraudCheck>> getAllFraudChecks() {
        List<FraudCheck> fraudChecks = fraudCheckRepository.findAll();
        return ResponseEntity.ok(fraudChecks);
    }

    // Obter verificação de fraude por ID
    @GetMapping("/{id}")
    public ResponseEntity<FraudCheck> getFraudCheckById(@PathVariable Long id) {
        Optional<FraudCheck> fraudCheck = fraudCheckRepository.findById(id);
        return fraudCheck.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar verificação de fraude
    @PutMapping("/{id}")
    public ResponseEntity<FraudCheck> updateFraudCheck(@PathVariable Long id, 
                                                        @Valid @RequestBody FraudCheckRequest request) {
        Optional<FraudCheck> existingFraudCheck = fraudCheckRepository.findById(id);
        if (existingFraudCheck.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        FraudCheck updatedFraudCheck = existingFraudCheck.get();
        Optional<Payment> payment = paymentRepository.findById(request.getPaymentId());
        if (payment.isPresent()) {
            updatedFraudCheck.setPayment(payment.get());
        } else {
            return ResponseEntity.badRequest().body(null); // Ou trate o erro de forma apropriada
        }
        updatedFraudCheck.setFraudStatus(request.getFraudStatus());
        updatedFraudCheck.setCheckReason(request.getCheckReason());
        updatedFraudCheck.setCheckedAt(ZonedDateTime.now());

        fraudCheckRepository.save(updatedFraudCheck);
        return ResponseEntity.ok(updatedFraudCheck);
    }

    // Excluir verificação de fraude
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFraudCheck(@PathVariable Long id) {
        if (!fraudCheckRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        fraudCheckRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
