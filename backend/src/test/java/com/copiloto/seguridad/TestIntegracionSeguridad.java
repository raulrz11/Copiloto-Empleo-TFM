package com.copiloto.seguridad;

import com.copiloto.TestcontainersConfiguration;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public class TestIntegracionSeguridad {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    void sinTokenDevuelve401() throws Exception {
        mockMvc.perform(get("/api/usuarios/yo"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void conTokenDelRegistroPuedeAcceder() throws Exception {
        String email = emailAleatorio();
        String token = registrar(email, "contraseña-segura");

        mockMvc.perform(get("/api/usuarios/yo").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void loginConContraseñaIncorrectaDevuelve401() throws Exception {
        String email = emailAleatorio();
        registrar(email, "contraseña-segura");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "%s", "password": "otra-contraseña"}
                                """.formatted(email)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registroConEmailRepetidoDevuelve409() throws Exception {
        String email = emailAleatorio();
        registrar(email, "contraseña-segura");

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoRegistro(email, "contraseña-segura")))
                .andExpect(status().isConflict());
    }

    @Test
    void laContrasenaSeGuardaHasheada() throws Exception {
        String email = emailAleatorio();
        registrar(email, "password-segura");

        Usuario guardado = usuarioRepository.findByEmail(email).orElseThrow();
        assertThat(guardado.getPasswordHash())
                .isNotEqualTo("password-segura")
                .startsWith("$2");
    }

    private String registrar(String email, String password) throws Exception {
        String respuesta = mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoRegistro(email, password)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(respuesta, "$.token");
    }

    private String cuerpoRegistro(String email, String password) {
        return """
                {"email": "%s", "password": "%s", "nombre": "Test"}
                """.formatted(email, password);
    }

    private String emailAleatorio() {
        return "test-" + UUID.randomUUID() + "@ejemplo.com";
    }

    // Revisar mas adelante por si fueran necesarios
    /*
    @Test
    void loginCorrectoDevuelveTokenValido() throws Exception {
        String email = emailAleatorio();
        registrar(email, "contraseña-segura");

        String respuesta = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "%s", "password": "contraseña-segura"}
                                """.formatted(email)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String token = JsonPath.read(respuesta, "$.token");
        mockMvc.perform(get("/api/usuarios/yo").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void loginConEmailInexistenteDevuelve401() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "%s", "password": "lo-que-sea-123"}
                                """.formatted(emailAleatorio())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void elEmailNoDistingueMayusculas() throws Exception {
        String email = emailAleatorio();
        registrar(email, "contraseña-segura");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "%s", "password": "contraseña-segura"}
                                """.formatted(email.toUpperCase())))
                .andExpect(status().isOk());
    }

    @Test
    void registroConDatosNoValidosDevuelve400() throws Exception {
        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoRegistro("no-es-un-email", "corta")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tokenFalsoDevuelve401() throws Exception {
        mockMvc.perform(get("/api/usuarios/yo").header("Authorization", "Bearer esto.no.es-un-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void healthEsPublico() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }
    */
}
