package com.example.kuit.controller;
import com.example.kuit.dto.request.LoginRequest;
import com.example.kuit.dto.request.ReissueRequest;
import com.example.kuit.dto.response.LoginResponse;
import com.example.kuit.dto.response.ReissueResponse;
import com.example.kuit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 요청 형식
     1. 일반 유저 로그인
         {
            "username" : "member1",
            "password" : "pass1234"
         }

     2. 관리자 로그인
         {
         "username" : "admin1",
         "password" : "pass1234"
         }
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        // TODO: 로그인 성공시 RefreshToken 까지 발급해보기
        LoginResponse response = authService.login(request.username(), request.password());

        return ResponseEntity.ok(response);
    }

    /**
     * 요청 형식
        {
            "refreshToken": "<JWT_REFRESH_TOKEN>"
        }

        Bearer 접두사 붙일 필요 X
     */
    @PostMapping("/reissue")
    public ResponseEntity<ReissueResponse> reissue(@RequestBody ReissueRequest request) {
        // TODO: reissue API 완성하기
        ReissueResponse response = authService.reissue(request.refreshToken());

        return ResponseEntity.ok(response);
    }
}
