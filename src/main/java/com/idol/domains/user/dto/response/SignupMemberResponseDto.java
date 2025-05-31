package com.idol.domains.user.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.idol.domains.user.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record SignupMemberResponseDto(
        @Schema(description = "회원 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID memberId,

        @Schema(description = "이메일", example = "member@example.com")
        String email,

        @Schema(description = "닉네임", example = "아이돌팬")
        String nickname,

        @Schema(description = "가입 일시", example = "2025-05-24T17:30:00")
        LocalDateTime createdAt
) {
    public static SignupMemberResponseDto from(Member member) {
        return SignupMemberResponseDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .createdAt(member.getCreatedAt())
                .build();
    }
}