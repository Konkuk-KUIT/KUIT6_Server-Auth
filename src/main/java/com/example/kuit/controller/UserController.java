package com.example.kuit.controller;

import com.example.kuit.dto.response.AdminResponse;
import com.example.kuit.dto.response.ProfileResponse;
import com.example.kuit.jwt.JwtUtil;
import com.example.kuit.model.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final JwtUtil jwtUtil;

    // GET /api/users/me  - 프로필 조회 API
    /**
     * 요청 형식
        Authorization 헤더: Bearer <Access Token>
     */
    @GetMapping("/me")
    public ProfileResponse me(HttpServletRequest request) {
        String token = extractBearer(request);

        if (!jwtUtil.validate(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }

        if (!jwtUtil.getTokenType(token).equals(TokenType.ACCESS)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access Token 이 필요합니다.");
        }

        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        return ProfileResponse.of(username, role);
    }

    // GET /api/users/admin - 관리자 확인 API (인가 필요)
    /**
     * 요청 형식
        Authorization 헤더: Bearer <Access Token>
     */
    @GetMapping("/admin")
    public AdminResponse admin(HttpServletRequest request) {
        // TODO: 토큰 추출 - extractBearer 메서드 활용

        // TODO: 토큰 유효성 검사 - jwtUtil.validate 메서드 활용

        // TODO: 토큰 타입 검사 - jwtUtil.getTokenType 메서드 활용

        // TODO: 토큰으로부터 유저 Role 추출 - jwtUtil.getRole 메서드 활용

        // TODO: 관리자 권한 검사 - 토큰으로부터 추출한 Role 이 Role.ROLE_ADMIN 과 동일한지 검증

        return AdminResponse.ok();
    }

    // 헤더로부터 토큰 추출
    private String extractBearer(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization 헤더 전송 형식이 잘못되었습니다.");
        }
        return header.substring(7);
    }
}
