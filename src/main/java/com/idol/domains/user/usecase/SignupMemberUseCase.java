package com.idol.domains.user.usecase;

import com.idol.domains.user.dto.request.SignupMemberRequestDto;
import com.idol.domains.user.dto.response.SignupMemberResponseDto;

public interface SignupMemberUseCase {

    SignupMemberResponseDto signup(SignupMemberRequestDto requestDto);
}
