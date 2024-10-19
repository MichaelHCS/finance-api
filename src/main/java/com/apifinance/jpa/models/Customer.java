package com.apifinance.jpa.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {

    @Column(nullable = false)
    @NotBlank // Validação para garantir que o nome não seja vazio
    @Size(min = 1, max = 100) // Validação de tamanho para o nome
    private String name; 

    @Column(nullable = false, unique = true)
    @NotBlank 
    @Email 
    private String email; 

    @Column(name = "phone_number", nullable = false)
    @NotBlank 
    @Size(min = 10, max = 15) 
    private String phoneNumber; 

    public Customer() {}
        
    public Customer(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    
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
            "id=" + getId() + 
            ",name='" + name + '\'' +
            ",email='" + email + '\'' +
            ",phoneNumber='" + phoneNumber + '\'' +
            '}';
    }
}
