package com.idol.domains.user.service;

import com.idol.domains.user.domain.Member;
import com.idol.domains.user.dto.request.SignupMemberRequestDto;
import com.idol.domains.user.dto.response.SignupMemberResponseDto;
import com.idol.domains.user.repository.MemberRepository;
import com.idol.domains.user.usecase.SignupMemberUseCase;
import com.idol.global.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class SignupMemberService implements SignupMemberUseCase {

    private final MemberRepository memberRepository;
    private final PasswordEncryptor passwordEncryptor;

    @Override
    public SignupMemberResponseDto signup(SignupMemberRequestDto requestDto) {

        validateDuplicateEmail(requestDto.email());
        validateDuplicateNickname(requestDto.nickname());

        String encryptedPassword = passwordEncryptor.encrypt(requestDto.password());

        Member member = memberRepository.save(Member.from(requestDto, encryptedPassword));

        return SignupMemberResponseDto.from(member);
    }

    private void validateDuplicateEmail(String email) {
        if (memberRepository.existsByMemberEmail(email)) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
    }

    private void validateDuplicateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }
    }
}
