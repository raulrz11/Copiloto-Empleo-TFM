package com.copiloto.seguridad;

import com.copiloto.seguridad.dto.UsuarioResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final AuthService authService;

    public UsuarioController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/yo")
    public UsuarioResponse yo(@AuthenticationPrincipal Jwt jwt) {
        return authService.obtener(Long.valueOf(jwt.getSubject()));
    }
}
