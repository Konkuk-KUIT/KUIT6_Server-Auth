package com.example.kuit.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final JdbcTemplate jdbc;

    public void save(String username, String token, Instant expiresAt) {
        jdbc.update(
                "INSERT INTO refresh_tokens (username, refresh_token, expires_at) VALUES (?, ?, ?)",
                username, token, java.sql.Timestamp.from(expiresAt)
        );
    }

    public Optional<String> findByUsername(String username) {
        List<String> list = jdbc.query(
                "SELECT refresh_token FROM refresh_tokens WHERE username = ?",
                (rs, rowNum) -> rs.getString("refresh_token"),
                username
        );

        return list.isEmpty()
                ? Optional.empty()
                : Optional.of(list.get(0));
    }

    public void deleteByUsername(String username) {
        jdbc.update("DELETE FROM refresh_tokens WHERE username = ?", username);
    }
}
