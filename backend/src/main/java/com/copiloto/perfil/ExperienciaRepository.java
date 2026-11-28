package com.copiloto.perfil;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExperienciaRepository extends JpaRepository<Experiencia, Long> {

    List<Experiencia> findByPerfilIdOrderByFechaInicioDesc(Long perfilId);

    Optional<Experiencia> findByIdAndPerfilId(Long id, Long perfilId);
}
