package com.apifinance.jpa.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Classe que representa um cliente.
 */
@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {

    @Column(nullable = false)
    @NotBlank // Validação para garantir que o nome não seja vazio
    @Size(min = 1, max = 100) // Validação de tamanho para o nome
    private String name; // Nome do cliente

    @Column(nullable = false, unique = true)
    @NotBlank // Validação para garantir que o email não seja vazio
    @Email // Validação para garantir que o email seja válido
    private String email; // Email do cliente

    @Column(name = "phone_number", nullable = false)
    @NotBlank // Validação para garantir que o número de telefone não seja vazio
    @Size(min = 10, max = 15) // Validação de tamanho para o número de telefone
    private String phoneNumber; // Número de telefone

    
    // Construtor padrão
    public Customer() {
        // O createdAt e updatedAt são gerenciados na classe BaseEntity
    }

    // Construtor com parâmetros
    public Customer(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() + // Considerando que BaseEntity tem um método getId()
            ",name='" + name + '\'' +
            ",email='" + email + '\'' +
            ",phoneNumber='" + phoneNumber + '\'' +
            '}';
    }
}
