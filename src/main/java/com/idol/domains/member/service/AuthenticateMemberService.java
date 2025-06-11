package com.idol.domains.member.service;

import com.idol.domains.auth.dto.request.LoginRequestDto;
import com.idol.domains.member.domain.Member;
import com.idol.domains.member.dto.response.AuthenticationResult;
import com.idol.domains.member.repository.MemberRepository;
import com.idol.domains.member.usecase.AuthenticateMemberUseCase;
import com.idol.global.exception.AuthenticationException;
import com.idol.global.exception.ExceptionMessage;
import com.idol.global.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticateMemberService implements AuthenticateMemberUseCase {

    private final MemberRepository memberRepository;
    private final PasswordEncryptor passwordEncryptor;

    @Override
    public AuthenticationResult authenticate(LoginRequestDto loginRequest) {
        Member member = memberRepository.findByMemberEmail(loginRequest.email())
                .orElseThrow(() -> new AuthenticationException(ExceptionMessage.AUTHENTICATION_FAILED));

        if (!passwordEncryptor.matches(loginRequest.password(), member.getPassword())) {
            throw new AuthenticationException(ExceptionMessage.AUTHENTICATION_FAILED);
        }

        return buildAuthenticationResult(member);
    }

    private AuthenticationResult buildAuthenticationResult(Member member) {
        Map<String, Object> additionalClaims = new HashMap<>();

        return AuthenticationResult.builder()
                .memberId(member.getMemberId().toString())
                .additionalClaims(additionalClaims)
                .build();
    }
}
