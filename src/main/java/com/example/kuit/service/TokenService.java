package com.example.kuit.service;

import com.example.kuit.dto.response.ReissueResponse;
import com.example.kuit.jwt.JwtUtil;
import com.example.kuit.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public ReissueResponse reissue(String refreshToken) {
        // TODO: 토큰 추출 - extractBearer 메서드 활용

        // TODO: 토큰 유효성 검사 - jwtUtil.validate 메서드 활용

        // TODO: 토큰 타입 검사 - jwtUtil.getTokenType 메서드 활용

        // TODO: DB에 RefreshToken 존재 여부 확인

        // TODO: 토큰 만료 여부 검사

        // TODO: AccessToken 재발급
        return ReissueResponse.of("accessToken");
    }
}
