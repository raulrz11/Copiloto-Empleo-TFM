package com.copiloto.seguridad;

import com.copiloto.seguridad.dto.LoginRequest;
import com.copiloto.seguridad.dto.RegistroRequest;
import com.copiloto.seguridad.dto.TokenResponse;
import com.copiloto.seguridad.dto.UsuarioResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    TokenResponse registro(RegistroRequest request) {
        String email = request.email();
        if (usuarioRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya existe");
        }
        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(request.password()))
                .nombre(request.nombre())
                .build());
        return tokenService.generar(usuario);
    }

    @Transactional(readOnly = true)
    TokenResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(normalizar(request.email()))
                .filter(u -> passwordEncoder.matches(request.password(), u.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email o contraseña incorrecto"));
        return tokenService.generar(usuario);
    }

    @Transactional(readOnly = true)
    UsuarioResponse obtener(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new UsuarioResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail());
    }

    private String normalizar(String email) {
        return email.trim().toLowerCase();
    }
}
