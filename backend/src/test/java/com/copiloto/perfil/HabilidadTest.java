package com.copiloto.perfil;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HabilidadTest {
    private final Perfil perfil = new Perfil(1L);

    @Test
    void crearHabilidadExitosaSinEspacios() {
        Habilidad habilidad = new Habilidad(perfil, "  Java  ");
        assertThat(habilidad.getNombre()).isEqualTo("Java");
        assertThat(habilidad.getPerfil()).isSameAs(perfil);
    }

    @Test
    void rechazarNombreNulo() {
        assertThatThrownBy(() -> new Habilidad(perfil, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void rechazarNombreVacio() {
        assertThatThrownBy(() -> new Habilidad(perfil, " ")).isInstanceOf(IllegalArgumentException.class);
    }
}
