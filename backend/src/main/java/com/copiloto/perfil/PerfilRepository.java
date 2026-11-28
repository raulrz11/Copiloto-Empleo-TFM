package com.copiloto.perfil;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// No hace falta poner @Repository porque JpaRepository ya lo tiene
// Solo es necesario ponerlo si queremos añadir un comportamiento adicional a la clase que no esté definido en JpaRepository
interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Optional<Perfil> findByUsuarioId(Long usuarioId);
}
