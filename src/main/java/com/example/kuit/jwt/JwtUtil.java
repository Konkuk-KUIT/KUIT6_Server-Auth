package com.example.kuit.jwt;

import com.example.kuit.model.Role;
import com.example.kuit.model.TokenType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.time.Instant;
import java.util.Date;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Getter
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    private final SecretKey key;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration-ms}") long accessTokenExpirationMs,
            @Value("${jwt.refresh-token-expiration-ms}") long refreshTokenExpirationMs
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    // 액세스 토큰 발급
    public String generateAccessToken(String username, String role) {
        return generateToken(username, role, TokenType.ACCESS, accessTokenExpirationMs);
    }

    // 리프레시 토큰 발급
    public String generateRefreshToken(String username, String role) {
        return generateToken(username, role, TokenType.REFRESH, refreshTokenExpirationMs);
    }

    private String generateToken(String username, String role, TokenType type, long expireMs) {
        Instant now = Instant.now();
        Instant exp = now.plusMillis(expireMs);

        return Jwts.builder()
                .header().type("JWT").and()
                .subject(username)
                .claim("role", role)
                .claim("type", type.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }


    public String getUsername(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public Role getRole(String token) {
        Object role = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role");

        return role == null ? null : Role.valueOf(role.toString());
    }

    public TokenType getTokenType(String token) {
        Object type = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("type");

        return type == null ? null : TokenType.valueOf(type.toString());
    }

    public Instant getExpiration(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.toInstant();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
