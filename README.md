# KUIT 6기 7주차 인증/인가 - 미션 설명

## 📋 미션 목록

### 1. UserController의 `/api/users/admin` API 완성하기
**목표**: 관리자 권한 인가 구현

**위치**: `UserController.java` - `admin()` 메서드

**구현해야 할 내용**
1. 토큰 추출
2. 토큰 유효성 검사
3. 토큰 타입 검사 - jwtUtil.getTokenType 메서드 활용
4. 토큰으로부터 유저 Role 추출 - jwtUtil.getRole 메서드 활용
5. 관리자 권한 검사 - 토큰으로부터 추출한 Role이 Role.ROLE_ADMIN과 동일한지 검증


**요구사항**
- JWT 토큰에서 사용자 역할(Role)을 추출
- 역할이 `ROLE_ADMIN`인 경우에만 접근 허용
- 권한이 없는 경우 적절한 예외 처리

---

### 2. AuthController의 `/api/auth/login` API 업그레이드하기
**목표**: RefreshToken까지 발급하여 로그인 기능 강화

**관련 파일들**
- `LoginResponse.java` - DTO 수정 필요
- `AuthService.java` - `login()` 메서드 수정 필요
- `AuthController.java` - `login()` 메서드

**구현해야 할 내용**

#### 2-1. LoginResponse DTO 수정
```java
// 현재: accessToken만 반환
public record LoginResponse(String accessToken)

// 수정 후: accessToken과 refreshToken 모두 반환
public record LoginResponse(String accessToken, String refreshToken)
```

#### 2-2. AuthService의 login 메서드 수정
- `jwtUtil.generateRefreshToken()` 메서드 활용
- 로그인한 유저에 대한 기존 RefreshToken, 즉 더이상 사용되지 않을 RefreshToken은 DB에서 삭제 (`RefreshTokenRepository.deleteByUsername()` 활용)
- 새로운 RefreshToken을 DB에 저장 (`RefreshTokenRepository.save()` 활용)
- LoginResponse에 두 토큰 모두 포함하여 반환

---

### 3. AuthController의 `/api/auth/reissue` API 구현하기
**목표**: AccessToken 만료시 RefreshToken을 활용해 AccessToken 재발급

**관련 파일들**
- `AuthService.java` - `reissue()` 메서드 구현
- `AuthController.java` - `reissue()` 메서드

**구현해야 할 내용**
1. 토큰 추출 - extractBearer 메서드 활용
2. 토큰 유효성 검사 - jwtUtil.validate 메서드 활용
3. 토큰 타입 검사 - jwtUtil.getTokenType 메서드 활용
4. DB에 RefreshToken 존재 여부 확인
5. 토큰 만료 여부 검사
6. AccessToken 재발급

**요구사항**
- RefreshToken의 유효성 검증
- DB에 저장된 RefreshToken과 일치하는지 확인
- 유효한 경우 새로운 AccessToken 발급

---

### 4. RefreshTokenRotation 구현하기 (선택)
**목표**: Reissue API를 통해 AccessToken 재발급시 RefreshToken도 재발급

**관련 파일들**
- `ReissueResponse.java` - DTO 수정 필요
- `AuthService.java` - `reissue()` 메서드 확장

**구현해야 할 내용**

#### 4-1. ReissueResponse DTO 수정
```java
// 현재: accessToken만 반환
public record ReissueResponse(String accessToken)

// 수정 후: accessToken과 refreshToken 모두 반환
public record ReissueResponse(String accessToken, String refreshToken)
```

#### 4-2. AuthService의 reissue 메서드 확장
- AccessToken 재발급과 함께 새로운 RefreshToken도 발급
- 기존 RefreshToken 삭제 후 새로운 RefreshToken DB 저장
- 보안성 향상을 위한 토큰 로테이션 구현

---

## 📚 학습 목표

이 미션들을 통해 다음을 학습해봅시다

1. **JWT 토큰 기반 인증 시스템**
2. **Role-based Authorization (RBAC)**
3. **AccessToken과 RefreshToken의 역할과 구현**
4. **토큰 재발급 메커니즘**
5. **RefreshToken Rotation 보안 패턴**

=> 반드시 PostMan 을 활용하여 직접 API 요청을 날려보고 응답을 받아보자!!

---

## ✉️ PostMan 요청 형식
1. 로그인 요청 POST /api/auth/login
```json
{
    "username": "member1",
    "password": "pass1234"
}
```

```json
{
    "username": "admin1",
    "password": "pass1234"
}
```


2. 프로필 조회 GET /api/users/me -> Authorization 헤더: Bearer <Access Token>


3. 관리자 확인 GET /api/users/admin -> Authorization 헤더: Bearer <Access Token>


4. reissue (토큰 재발급) POST /api/auth/reissue
```json
  {
      "refreshToken": "<JWT REFRESH TOKEN>"
  }
```
