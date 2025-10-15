package com.example.kuit.repository;

import com.example.kuit.model.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final JdbcTemplate jdbc;

    private static final RowMapper<RefreshToken> MAPPER = (rs, rn) ->
            new RefreshToken(
                    rs.getString("username"),
                    rs.getString("refresh_token"),
                    rs.getTimestamp("expires_at").toInstant()
            );

    public void save(RefreshToken token) {
        jdbc.update(
                "INSERT INTO refresh_tokens (username, refresh_token, expires_at) VALUES (?, ?, ?)",
                token.username(), token.token(), Timestamp.from(token.expiresAt())
        );
    }

    public Optional<RefreshToken> findByUsername(String username) {
        List<RefreshToken> list = jdbc.query(
                "SELECT username, refresh_token, expires_at FROM refresh_tokens WHERE username = ?",
                MAPPER, username
        );
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public void deleteByUsername(String username) {
        jdbc.update("DELETE FROM refresh_tokens WHERE username = ?", username);
    }
}
