package com.example.kuit.service;

import com.example.kuit.dto.response.LoginResponse;
import com.example.kuit.dto.response.ProfileResponse;
import com.example.kuit.jwt.JwtUtil;
import com.example.kuit.model.User;
import com.example.kuit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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

    public ProfileResponse getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        return ProfileResponse.from(user);
    }
}
