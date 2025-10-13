package com.example.kuit.repository;

import java.sql.ResultSet;
import java.util.Optional;

import com.example.kuit.model.Role;
import com.example.kuit.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    private static final RowMapper<User> MAPPER = (ResultSet rs, int rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password"),
            Role.valueOf(rs.getString("role"))
    );

    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query(
                "SELECT id, username, password, role FROM users WHERE username = ?",
                MAPPER,
                username
        ).stream().findFirst();
    }
}
