package com.idol.domains.member.usecase;

import com.idol.domains.member.dto.request.SignupMemberRequestDto;
import com.idol.domains.member.dto.response.SignupMemberResponseDto;

public interface SignupMemberUseCase {
    SignupMemberResponseDto signup(SignupMemberRequestDto requestDto);
}
