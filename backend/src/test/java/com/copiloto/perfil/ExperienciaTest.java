package com.copiloto.perfil;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ExperienciaTest {
    private final Perfil perfil = new Perfil(1L);

    @Test
    void crearExperienciaExitosa() {
        Experiencia experiencia = Experiencia.builder()
                .perfil(perfil)
                .tipo(TipoExperiencia.FORMACION)
                .titulo(" Master en Inteligencia Artificial  ")
                .fechaInicio(LocalDate.of(2020, 1, 1))
                .fechaFin(LocalDate.of(2022, 12, 31))
                .build();

        assertThat(experiencia.getTitulo()).isEqualTo("Master en Inteligencia Artificial");
        assertThat(experiencia.getHabilidades()).isEmpty();

    }

    @Test
    void rechazaQueLaFechaDeFinSeaAnteriorALaDeInicio() {
        assertThatThrownBy(() -> Experiencia.builder()
                .perfil(perfil)
                .tipo(TipoExperiencia.LABORAL)
                .titulo("Prácticas")
                .fechaInicio(LocalDate.of(2025, 6, 1))
                .fechaFin(LocalDate.of(2025, 1, 1))
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rechazaUnTituloVacio() {
        assertThatThrownBy(() -> Experiencia.builder()
                .perfil(perfil)
                .tipo(TipoExperiencia.LABORAL)
                .titulo("   ")
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rechazaUnTituloNulo() {
        assertThatThrownBy(() -> Experiencia.builder()
                .perfil(perfil)
                .tipo(TipoExperiencia.LABORAL)
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rechazaUnTipoNulo() {
        assertThatThrownBy(() -> Experiencia.builder()
                .perfil(perfil)
                .titulo("Prácticas")
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void actualizarCambiaLosDatosYCopiaLaLista() {
        Experiencia experiencia = Experiencia.builder()
                .perfil(perfil)
                .tipo(TipoExperiencia.PROYECTO)
                .titulo("Versión 1")
                .build();

        List<String> habilidades = new ArrayList<>(List.of("Java"));
        experiencia.actualizar(TipoExperiencia.PROYECTO, "Versión 2", null, null,
                null, null, null, habilidades);
        habilidades.add("Docker");   // modificar la lista original no debe afectar a la entidad

        assertThat(experiencia.getTitulo()).isEqualTo("Versión 2");
        assertThat(experiencia.getHabilidades()).containsExactly("Java");
    }
}
