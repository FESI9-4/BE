package com.idol.domains.member.service.command;

import com.idol.domains.member.domain.Member;
import com.idol.domains.member.dto.request.SignupMemberRequestDto;
import com.idol.domains.member.dto.response.SignupMemberResponseDto;
import com.idol.domains.member.repository.MemberRepository;
import com.idol.domains.member.usecase.SignupMemberUseCase;
import com.idol.global.exception.ConflictException;
import com.idol.global.exception.ExceptionMessage;
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
            throw new ConflictException(ExceptionMessage.EMAIL_ALREADY_EXISTS);
        }
    }

    private void validateDuplicateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new ConflictException(ExceptionMessage.NICKNAME_ALREADY_EXISTS);
        }
    }
}
