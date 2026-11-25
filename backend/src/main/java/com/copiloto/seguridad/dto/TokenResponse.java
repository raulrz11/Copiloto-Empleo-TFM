package com.copiloto.seguridad.dto;

import java.time.Instant;

public record TokenResponse(String token, Instant expiration) {
}
