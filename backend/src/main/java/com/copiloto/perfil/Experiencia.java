package com.copiloto.perfil;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "experiencia")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"perfil", "descripcion"})
public class Experiencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfil_id", nullable = false, updatable = false)
    private Perfil perfil;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private TipoExperiencia tipo;

    @Column(length = 200, nullable = false)
    private String titulo;

    @Column(length = 200)
    private String organizacion;

    @Column(length = 500)
    private String url;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    private String descripcion;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private List<String> habilidades = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at",  nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",  nullable = false)
    private Instant updatedAt;

    @Builder
    public Experiencia(Perfil perfil, TipoExperiencia tipo, String titulo, String organizacion, String url, LocalDate fechaInicio, LocalDate fechaFin, String descripcion, List<String> habilidades) {
        // Objects.requiereNonNull, lanza una excepción con el mensaje "El perfil no puede ser nulo" si el perfil es nulo
        this.perfil = Objects.requireNonNull(perfil, "El perfil no puede ser nulo");
        actualizar(tipo , titulo, organizacion, url, fechaInicio, fechaFin, descripcion, habilidades);
    }

    // Un metodo actualizar en este caso porque
    public void actualizar(TipoExperiencia tipo, String titulo, String organizacion, String url, LocalDate fechaInicio, LocalDate fechaFin, String descripcion, List<String> habilidades) {
        if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        this.tipo = Objects.requireNonNull(tipo, "El tipo no puede ser nulo");
        this.titulo = textoObligatorio(titulo, "El título no puede ser nulo o vacío");
        this.organizacion = organizacion;
        this.url = url;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.habilidades = habilidades != null ? new ArrayList<>(habilidades) : new ArrayList<>();
    }


    // Metodo para validar que el texto no sea nulo o vacío
    // Habria que ponerlo en el metodo actualizar en los campos que se requieran validar el texto, por ejemplo titulo.

    // Esta estructua puede sustitur a la del constructor de la entidad cuyo campos necesiten validacion interna (Ej: Habilidad, en el nombre)
    private static String textoObligatorio(String valor, String mensaje) {
        Objects.requireNonNull(valor, mensaje);
        if (valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor.trim();
    }
}
