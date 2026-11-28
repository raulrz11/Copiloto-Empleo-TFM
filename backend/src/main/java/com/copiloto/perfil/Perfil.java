package com.copiloto.perfil;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "perfil")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = "cvTexto")
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false, unique = true, updatable = false)
    private Long usuarioId;

    @Column(length = 150)
    private String titulo;

    private String resumen;

    @Column(name = "cv_texto")
    private String cvTexto;

    @CreationTimestamp
    @Column(name = "created_at",  nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",  nullable = false)
    private Instant updatedAt;


    public Perfil(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void actualizarDatos(String titulo, String resumen) {
        this.titulo = titulo;
        this.resumen = resumen;
    }

    public void guardarTextoCv(String cvTexto) {
        this.cvTexto = cvTexto;
    }
}
