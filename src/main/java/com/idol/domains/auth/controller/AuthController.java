package com.idol.domains.auth.controller;

import com.idol.domains.auth.config.JwtProperties;
import com.idol.domains.auth.dto.request.LoginRequestDto;
import com.idol.domains.auth.service.JwtService;
import com.idol.domains.member.dto.response.AuthenticationResult;
import com.idol.domains.member.usecase.AuthenticateMemberUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "AUTH API", description = "로그인/로그아웃 API")
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final AuthenticateMemberUseCase authenticateMemberUseCase;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        AuthenticationResult authResult = authenticateMemberUseCase.authenticate(loginRequest);

        String accessToken = jwtService.generateAccessToken(authResult.getMemberId());
        String refreshToken = jwtService.generateRefreshToken(authResult.getMemberId());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                //.secure(true) // HTTPS 환경에서만 동작 설정 추후 배포후 적용예정
                .path("/api/auth/refresh")
                .maxAge(jwtProperties.getRefreshTokenExpiration())
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header("Access-Control-Expose-Headers", HttpHeaders.AUTHORIZATION)
                .body("로그인 성공");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Refresh token not found"));
        }

        if (!jwtService.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid refresh token"));
        }

        String memberId = jwtService.extractMemberId(refreshToken);
        String newAccessToken = jwtService.generateAccessToken(memberId);
        String newRefreshToken = jwtService.generateRefreshToken(memberId);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                //.secure(true)
                .path("/")
                .maxAge(jwtProperties.getRefreshTokenExpiration())
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken)
                .header("Access-Control-Expose-Headers", HttpHeaders.AUTHORIZATION)
                .body("토큰 리프레쉬 성공");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {

        if (refreshToken != null && jwtService.isTokenValid(refreshToken)) {
            String memberId = jwtService.extractMemberId(refreshToken);
            jwtService.revokeRefreshToken(memberId);
        }

        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/api/auth/refresh")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body("로그아웃 성공");
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false, "message", "No token provided"));
        }

        String token = authHeader.substring(7);
        boolean isValid = jwtService.isTokenValid(token);

        if (isValid) {
            String memberId = jwtService.extractMemberId(token);
            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "memberId", memberId
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                    "valid", false,
                    "message", "Invalid or expired token"
            ));
        }
    }

}
