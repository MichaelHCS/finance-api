package com.apifinance.jpa.models;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

/**
 * Classe base que contém campos comuns para entidades.
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id; // ID da entidade

    @Column(name = "created_at", nullable = false, updatable = false)
    protected ZonedDateTime createdAt; // Data/hora de criação

    @Column(name = "updated_at", nullable = false)
    protected ZonedDateTime updatedAt; // Data/hora da última atualização

    @Column(name = "checked_at") // Adicionei a anotação para o campo checkedAt
    protected ZonedDateTime checkedAt; // Data/hora da verificação

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now(); // Define a data/hora atual
        updatedAt = createdAt; // Inicializa updatedAt com o mesmo valor de createdAt
        checkedAt = null; // Inicializa checkedAt como nulo
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now(); // Atualiza updatedAt com a data/hora atual
    }

    // Getters
    public Long getId() {
        return id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ZonedDateTime getCheckedAt() { // Getter para checkedAt
        return checkedAt;
    }

    public void setCheckedAt(ZonedDateTime checkedAt) { // Setter para checkedAt
        this.checkedAt = checkedAt;
    }

    // Override equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;
        BaseEntity that = (BaseEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
