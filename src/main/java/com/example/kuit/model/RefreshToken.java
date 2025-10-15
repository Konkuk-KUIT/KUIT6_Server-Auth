package com.example.kuit.model;

import java.time.Instant;

public record RefreshToken(
        String username,
        String token,
        Instant expiresAt
) {
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
