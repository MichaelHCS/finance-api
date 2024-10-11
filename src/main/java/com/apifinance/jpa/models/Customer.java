package com.apifinance.jpa.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {

    @Column(nullable = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @Column(nullable = false)
    @NotNull
    @Email
    private String email;

    @Column(name = "phone_number", nullable = false)
    @NotNull
    @Size(min = 10, max = 15)
    private String phoneNumber;

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
}
