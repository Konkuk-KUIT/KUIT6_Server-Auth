package com.example.kuit.dto.response;

public record ProfileResponse(
        String username,
        String role
) {
    public static ProfileResponse of(String username, String role) {
        return new ProfileResponse(username, role);
    }
}
