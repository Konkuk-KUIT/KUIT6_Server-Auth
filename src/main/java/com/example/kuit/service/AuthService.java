package com.example.kuit.service;

import com.example.kuit.dto.response.LoginResponse;
import com.example.kuit.dto.response.ReissueResponse;
import com.example.kuit.jwt.JwtUtil;
import com.example.kuit.model.User;
import com.example.kuit.repository.RefreshTokenRepository;
import com.example.kuit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public LoginResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        if (!user.password().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.generateAccessToken(username, user.role().name());

        return LoginResponse.of(accessToken);
    }

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
