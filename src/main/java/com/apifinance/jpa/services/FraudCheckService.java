package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckResult;
import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.repositories.FraudCheckRepository;
import com.apifinance.jpa.repositories.PaymentRepository;

import jakarta.transaction.Transactional;

@Service
public class FraudCheckService {

    @Autowired
    private FraudCheckRepository fraudCheckRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    public FraudCheck createFraudCheck(Long paymentId, FraudCheckResult fraudStatus, FraudCheckReason checkReason) {
        // Buscando o pagamento associado
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado com ID: " + paymentId));

        // Atualizando o status do pagamento
        payment.setStatus(fraudStatus == FraudCheckResult.APPROVED ? PaymentStatus.APPROVED : PaymentStatus.REJECTED);
        paymentRepository.save(payment);

        // Criando uma nova instância de FraudCheck
        FraudCheck fraudCheck = new FraudCheck();
        fraudCheck.setPayment(payment);
        fraudCheck.setFraudStatus(fraudStatus); // Certifique-se de passar um valor válido
        fraudCheck.setCheckReason(checkReason); // Agora é do tipo FraudCheckReason
        fraudCheck.setFraudCheckedAt(ZonedDateTime.now()); // Definindo a data/hora atual

        // Persistindo a verificação de fraude
        return fraudCheckRepository.save(fraudCheck);
    }

    // Método para obter todas as verificações de fraude
    public List<FraudCheck> getAllFraudChecks() {
        return fraudCheckRepository.findAll();
    }

    // Método para obter verificação de fraude por ID
    public Optional<FraudCheck> getFraudCheckById(Long id) {
        return fraudCheckRepository.findById(id);
    }

    public FraudCheck updateFraudCheck(Long id, Long paymentId, FraudCheckResult fraudStatus, FraudCheckReason checkReason) {
        // Verifique se o pagamento existe
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado com ID: " + paymentId));

        // Criar ou atualizar a verificação de fraude
        FraudCheck fraudCheck = new FraudCheck();
        fraudCheck.setId(id); // Defina o ID
        fraudCheck.setPayment(payment);
        fraudCheck.setFraudStatus(fraudStatus);
        fraudCheck.setCheckReason(checkReason);
        fraudCheck.setFraudCheckedAt(ZonedDateTime.now()); // Atualiza a data/hora

        return fraudCheckRepository.save(fraudCheck);
    }

    public void deleteFraudCheck(Long id) {
        if (!existsById(id)) {
            throw new IllegalArgumentException("FraudCheck não encontrado com ID: " + id);
        }
        fraudCheckRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return fraudCheckRepository.existsById(id);
    }
}
