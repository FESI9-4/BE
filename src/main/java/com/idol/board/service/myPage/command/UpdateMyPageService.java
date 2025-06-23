package com.idol.board.service.myPage.command;

import com.idol.board.dto.request.myPage.MyPagePasswordUpdateRequestDto;
import com.idol.board.dto.request.myPage.MyPageUpdateRequestDto;
import com.idol.board.usecase.mypage.command.UpdateMyPageUseCase;
import com.idol.domains.member.domain.Member;
import com.idol.domains.member.repository.MemberJpaRepository;
import com.idol.global.exception.ConflictException;
import com.idol.global.exception.ExceptionMessage;
import com.idol.global.exception.NotFoundException;
import com.idol.global.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateMyPageService implements UpdateMyPageUseCase {

    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncryptor passwordEncryptor;


    @Override
    public Long updateProfile(MyPageUpdateRequestDto dto, Long userId) {
        Member member = memberJpaRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Member", userId));

        member.update(dto);

        return userId;
    }

    @Override
    public Long updatePassword(MyPagePasswordUpdateRequestDto dto, Long userId) {
        Member member = memberJpaRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Member", userId));

        if( passwordEncryptor.matches(dto.password(), member.getPassword())){
            member.updatePassword(passwordEncryptor.encrypt(dto.newPassword()));
        }else{
            throw new ConflictException(ExceptionMessage.INCORRECT_PASSWORD);
        }

        return member.getMemberId();
    }
}
