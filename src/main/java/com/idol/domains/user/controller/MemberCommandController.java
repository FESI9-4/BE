package com.idol.domains.user.controller;

import com.idol.domains.user.dto.request.SignupMemberRequestDto;
import com.idol.domains.user.dto.response.SignupMemberResponseDto;
import com.idol.domains.user.usecase.SignupMemberUseCase;
import com.idol.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 CUD API", description = "회원 CUD API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class MemberCommandController {

    private final SignupMemberUseCase signupMemberUseCase;

    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 닉네임으로 회원가입을 진행합니다.")
    @PostMapping("/signup")
    public ApiResponse<SignupMemberResponseDto> signup(
            @Valid @RequestBody SignupMemberRequestDto requestDto
    ) {
        SignupMemberResponseDto response = signupMemberUseCase.signup(requestDto);
        return ApiResponse.ok(201, response, "회원가입 성공");
    }
}
