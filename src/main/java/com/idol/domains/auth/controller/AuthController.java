package com.idol.domains.auth.controller;

import com.idol.domains.auth.dto.LoginRequestDto;
import com.idol.domains.auth.service.JwtTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "AUTH API", description = "로그인/로그아웃 API")
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final JwtTokenService jwtTokenService;

    // TODO 책임 분리
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        // TODO: 인증로직 구현
        // 예: userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword())

        String username = loginRequest.email();

        // TODO 임시값 인증 구현후 설정
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", "12345");

        String accessToken = jwtTokenService.generateTokenWithClaims(username, claims);
        String refreshToken = jwtTokenService.generateRefreshToken(username);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                //.secure(true) // HTTPS 환경에서만 동작 설정 추후 배포후 적용예정
                .path("/api/auth/refresh")

                //TODO 상수화
                .maxAge(7 * 24 * 60 * 60) // 7일
                .sameSite("Strict")
                .build();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("username", username);
        response.put("expiresIn", 3600);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header("Access-Control-Expose-Headers", HttpHeaders.AUTHORIZATION)
                .body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Refresh token not found"));
        }

        if (!jwtTokenService.isTokenValid(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid refresh token"));
        }

        String username = jwtTokenService.extractUsername(refreshToken);

        String newAccessToken = jwtTokenService.generateAccessToken(username);

        String newRefreshToken = jwtTokenService.generateRefreshToken(username);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(7 * 24 * 60 * 60) // 7일
                .sameSite("Strict")
                .build();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Token refreshed successfully");
        response.put("expiresIn", 3600);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken)
                .header("Access-Control-Expose-Headers", HttpHeaders.AUTHORIZATION)
                .body(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false, "message", "No token provided"));
        }

        String token = authHeader.substring(7);
        boolean isValid = jwtTokenService.isTokenValid(token);

        if (isValid) {
            String username = jwtTokenService.extractUsername(token);
            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "username", username
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                    "valid", false,
                    "message", "Invalid or expired token"
            ));
        }
    }

}
