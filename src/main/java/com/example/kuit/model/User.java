package com.example.kuit.model;

public record User(
        Long id,
        String username,
        String password,
        Role role
) { }