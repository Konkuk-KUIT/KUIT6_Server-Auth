package com.example.kuit.dto.response;

import com.example.kuit.model.Role;

public record AdminResponse(
        String message,
        String role
) {
    public static AdminResponse ok() {
        return new AdminResponse("관리자님 환영합니다.", Role.ROLE_ADMIN.getValue());
    }
}
