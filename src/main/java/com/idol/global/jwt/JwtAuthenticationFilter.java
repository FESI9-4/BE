package com.idol.global.jwt;

import com.idol.domains.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/api/auth/",
            "/api/member/signup",
            "/swagger-ui/",
            "/v3/api-docs/",
            "/actuator/"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        log.debug("JWT Authentication Filter - Request URI: {}", requestUri);

        try {
            String jwt = extractTokenFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                processAuthentication(jwt, request);
            }
        } catch (Exception e) {
            log.error("JWT 인증 처리 중 오류 발생 - URI: {}, Error: {}", requestUri, e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private void processAuthentication(String jwt, HttpServletRequest request) {
        if (!jwtService.isTokenValid(jwt)) {
            log.debug("유효하지 않은 JWT 토큰");
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            log.debug("이미 인증된 요청");
            return;
        }

        try {
            String username = jwtService.extractMemberId(jwt);
            if (username == null) {
                log.warn("JWT 토큰에서 사용자명을 추출할 수 없음");
                return;
            }

            String userId = extractUserIdClaim(jwt);
            String role = extractRoleClaim(jwt);

            UsernamePasswordAuthenticationToken authToken = createAuthenticationToken(username, role);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);

            log.info("JWT 인증 성공 - username: {}, userId: {}, role: {}", username, userId, role);
        } catch (Exception e) {
            log.error("JWT 인증 정보 처리 실패: {}", e.getMessage());
        }
    }

    private String extractUserIdClaim(String jwt) {
        try {
            return jwtService.extractClaim(jwt, "userId", String.class);
        } catch (Exception e) {
            log.debug("userId 클레임 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    private String extractRoleClaim(String jwt) {
        try {
            String role = jwtService.extractClaim(jwt, "role", String.class);
            return role != null ? role : "USER";
        } catch (Exception e) {
            log.debug("role 클레임 추출 실패, 기본값 사용: {}", e.getMessage());
            return "USER";
        }
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(String username, String role) {
        String authorityRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(authorityRole)
        );

        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                authorities
        );
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        String paramToken = request.getParameter("token");
        if (StringUtils.hasText(paramToken)) {
            log.debug("쿼리 파라미터에서 토큰 추출");
            return paramToken;
        }

        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return EXCLUDED_PATHS.stream()
                .anyMatch(path::startsWith);
    }
}
