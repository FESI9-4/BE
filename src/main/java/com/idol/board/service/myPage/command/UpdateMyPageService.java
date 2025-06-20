package com.idol.board.service.myPage.command;

import com.idol.board.dto.request.myPage.MyPageUpdateRequestDto;
import com.idol.board.usecase.mypage.UpdateMyPageUseCase;
import com.idol.domains.member.domain.Member;
import com.idol.domains.member.repository.MemberJpaRepository;
import com.idol.domains.member.repository.MemberRepository;
import com.idol.global.exception.NotFoundException;
import com.idol.global.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateMyPageService implements UpdateMyPageUseCase {

    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncryptor passwordEncryptor;


    @Override
    public Long updateProfile(MyPageUpdateRequestDto dto, Long userId) {
        Member member = memberJpaRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Member", userId));

        String encryptedPassword = passwordEncryptor.encrypt(dto.password());

        member.update(dto, encryptedPassword);

        return userId;
    }
}
