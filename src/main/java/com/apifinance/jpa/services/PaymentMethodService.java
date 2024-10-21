package com.apifinance.jpa.services;

import java.util.List;
import java.util.Optional; // Supondo que você tenha um PaymentMethodRepository

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.models.PaymentMethod;
import com.apifinance.jpa.repositories.PaymentMethodRepository;

@Service
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    @Autowired
    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public List<PaymentMethod> findAll() {
        return paymentMethodRepository.findAll();
    }

    public PaymentMethod findById(Long id) {
        Optional<PaymentMethod> optionalPaymentMethod = paymentMethodRepository.findById(id);
        return optionalPaymentMethod.orElse(null); // Retorna null se não encontrar
    }

    public PaymentMethod save(PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    public PaymentMethod update(Long id, PaymentMethod paymentMethod) {
        if (paymentMethodRepository.existsById(id)) {
            paymentMethod.setId(id); // Define o ID do método de pagamento a ser atualizado
            return paymentMethodRepository.save(paymentMethod);
        }
        return null; // Retorna null se o método de pagamento não existir
    }

    public boolean delete(Long id) {
        if (paymentMethodRepository.existsById(id)) {
            paymentMethodRepository.deleteById(id);
            return true;
        }
        return false; // Retorna false se o método de pagamento não existir
    }
}
