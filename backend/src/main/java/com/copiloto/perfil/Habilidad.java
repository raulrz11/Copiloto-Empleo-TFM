package com.copiloto.perfil;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "habilidad")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "perfil")
public class Habilidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfil_id", nullable = false, updatable = false)
    private Perfil perfil;

    @Column(nullable = false, length = 100)
    private String nombre;

    // Solo se puede crear o eliminar, no se puede actualizar
    @CreationTimestamp
    @Column(name = "created_at",  nullable = false, updatable = false)
    private Instant createdAt;

    public Habilidad(Perfil perfil, String nombre) {
        this.perfil = perfil;
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo").trim();
        if (this.nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre de la habilidad no puede estar vacío");
        }
    }
}
