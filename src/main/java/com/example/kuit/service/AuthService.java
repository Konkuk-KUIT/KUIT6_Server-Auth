package com.example.kuit.service;

import com.example.kuit.dto.response.LoginResponse;
import com.example.kuit.dto.response.ReissueResponse;
import com.example.kuit.jwt.JwtUtil;
import com.example.kuit.model.Role;
import com.example.kuit.model.RefreshToken;
import com.example.kuit.model.User;
import com.example.kuit.repository.RefreshTokenRepository;
import com.example.kuit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

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

        // 리프레시 토큰 생성
        String refreshToken = jwtUtil.generateRefreshToken(username, user.role().name());

        // 만료 시간 추출
        Instant refreshExp = jwtUtil.getExpiration(refreshToken);

        // DB 에 리프레시 토큰 저장
        refreshTokenRepository.save(new RefreshToken(username, refreshToken, refreshExp));

        // 리프레시 토큰까지 발급
        return LoginResponse.of(accessToken, refreshToken);
    }

    public ReissueResponse reissue(String username, Role role, String refreshToken) {
        RefreshToken storedRefreshToken = refreshTokenRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 존재하지 않습니다."));

        if(storedRefreshToken.isExpired()) {
            refreshTokenRepository.deleteByUsername(username);

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다.");
        }

        if (!storedRefreshToken.token().equals(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 리프레시 토큰입니다.");
        }

        String newAccessToken = jwtUtil.generateAccessToken(username, role.name());

        return ReissueResponse.of(newAccessToken);
    }
}
