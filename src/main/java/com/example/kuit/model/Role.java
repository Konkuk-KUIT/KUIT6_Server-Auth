package com.example.kuit.model;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_USER("일반 유저"),
    ROLE_ADMIN("관리자");

    Role(String value) {
        this.value = value;
    }

    private final String value;
}
