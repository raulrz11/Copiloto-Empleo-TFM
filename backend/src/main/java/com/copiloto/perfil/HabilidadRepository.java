package com.copiloto.perfil;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabilidadRepository extends JpaRepository<Habilidad, Long> {

    List<Habilidad> findByPerfilIdOrderByNombreAsc(Long perfilId);

    Optional<Habilidad> findByIdAndPerfilId(Long id, Long perfilId);

    // Esto se usa para comprobar si ya existe una habilidad con el mismo nombre (ignorando mayúsculas y minúsculas) para un perfil dado, antes de crear una nueva habilidad.
    boolean existsByPerfilIdAndNombreIgnoreCase(Long perfilId, String nombre);
}
