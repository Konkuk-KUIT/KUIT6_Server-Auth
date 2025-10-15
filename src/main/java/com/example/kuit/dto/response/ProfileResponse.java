package com.example.kuit.dto.response;

import com.example.kuit.model.User;

public record ProfileResponse(
        Long id,
        String username,
        String password,
        String role
) {
    public static ProfileResponse from(User user) {
        return new ProfileResponse(
                user.id(),
                user.username(),
                user.password(),
                user.role().getValue());
    }
}
