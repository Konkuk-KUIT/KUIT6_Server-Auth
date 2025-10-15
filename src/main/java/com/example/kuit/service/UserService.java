package com.example.kuit.service;

import com.example.kuit.dto.response.ProfileResponse;
import com.example.kuit.model.User;
import com.example.kuit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ProfileResponse getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        return ProfileResponse.from(user);
    }
}
