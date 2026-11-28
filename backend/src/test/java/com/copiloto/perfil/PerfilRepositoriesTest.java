package com.copiloto.perfil;

import com.copiloto.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
// Al terminar cada test, todo lo que ha guardado se deshace (rollback). Cada test empieza limpio sin tener que borrar nada a mano.
@Transactional
public class PerfilRepositoriesTest {
    @Autowired PerfilRepository perfilRepository;
    @Autowired ExperienciaRepository experienciaRepository;
    @Autowired HabilidadRepository habilidadRepository;
    @Autowired JdbcTemplate jdbc;

    // ---------- Perfil ----------

    @Test
    void encuentraElPerfilPorUsuario() {
        Long usuarioId = crearUsuario();
        Perfil perfil = perfilRepository.save(new Perfil(usuarioId));

        assertThat(perfilRepository.findByUsuarioId(usuarioId))
                .get()
                .extracting(Perfil::getId)
                .isEqualTo(perfil.getId());
    }

    @Test
    void unUsuarioNoPuedeTenerDosPerfiles() {
        Long usuarioId = crearUsuario();
        perfilRepository.save(new Perfil(usuarioId));

        assertThatThrownBy(() -> perfilRepository.saveAndFlush(new Perfil(usuarioId)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    // ---------- Experiencia ----------

    @Test
    void guardaYLeeLasHabilidadesComoLista() {
        Perfil perfil = crearPerfil();
        Experiencia guardada = experienciaRepository.saveAndFlush(Experiencia.builder()
                .perfil(perfil)
                .tipo(TipoExperiencia.PROYECTO)
                .titulo("App de gestión")
                .habilidades(List.of("Java", "Spring Boot", "Docker"))
                .build());

        Experiencia leida = experienciaRepository.findById(guardada.getId()).orElseThrow();
        assertThat(leida.getHabilidades()).containsExactly("Java", "Spring Boot", "Docker");
        assertThat(leida.getTipo()).isEqualTo(TipoExperiencia.PROYECTO);
    }

    @Test
    void listaLasExperienciasDeLaMasRecienteALaMasAntigua() {
        Perfil perfil = crearPerfil();
        guardarExperiencia(perfil, "DAM", LocalDate.of(2022, 9, 1));
        guardarExperiencia(perfil, "Prácticas", LocalDate.of(2024, 3, 1));

        List<Experiencia> experiencias = experienciaRepository.findByPerfilIdOrderByFechaInicioDesc(perfil.getId());

        assertThat(experiencias).extracting(Experiencia::getTitulo)
                .containsExactly("Prácticas", "DAM");
    }

    @Test
    void noEncuentraLaExperienciaDeOtroPerfil() {
        Perfil mio = crearPerfil();
        Perfil otro = crearPerfil();
        Experiencia ajena = guardarExperiencia(otro, "Trabajo de otro", null);

        assertThat(experienciaRepository.findByIdAndPerfilId(ajena.getId(), mio.getId())).isEmpty();
    }

    // ---------- Habilidad ----------

    @Test
    void detectaHabilidadRepetidaSinDistinguirMayusculas() {
        Perfil perfil = crearPerfil();
        habilidadRepository.save(new Habilidad(perfil, "PostgreSQL"));

        assertThat(habilidadRepository.existsByPerfilIdAndNombreIgnoreCase(perfil.getId(), "postgresql"))
                .isTrue();
    }

    @Test
    void noPermiteLaMismaHabilidadDosVecesEnElMismoPerfil() {
        Perfil perfil = crearPerfil();
        habilidadRepository.saveAndFlush(new Habilidad(perfil, "Java"));

        assertThatThrownBy(() -> habilidadRepository.saveAndFlush(new Habilidad(perfil, "Java")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    // ---------- Auxiliares ----------

    private Long crearUsuario() {
        return jdbc.queryForObject(
                "INSERT INTO usuario (email, password_hash) VALUES (?, 'x') RETURNING id",
                Long.class, "test-" + UUID.randomUUID() + "@ejemplo.com");
    }

    private Perfil crearPerfil() {
        return perfilRepository.save(new Perfil(crearUsuario()));
    }

    private Experiencia guardarExperiencia(Perfil perfil, String titulo, LocalDate fechaInicio) {
        return experienciaRepository.saveAndFlush(Experiencia.builder()
                .perfil(perfil)
                .tipo(TipoExperiencia.LABORAL)
                .titulo(titulo)
                .fechaInicio(fechaInicio)
                .build());
    }
}
