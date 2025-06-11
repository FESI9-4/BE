package com.idol.domains.member.usecase;

import com.idol.domains.auth.dto.request.LoginRequestDto;
import com.idol.domains.member.dto.response.AuthenticationResult;

public interface AuthenticateMemberUseCase {
    AuthenticationResult authenticate(LoginRequestDto loginRequest);
}
