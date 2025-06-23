package com.idol.board.usecase.mypage.command;

import com.idol.board.dto.request.myPage.MyPagePasswordUpdateRequestDto;
import com.idol.board.dto.request.myPage.MyPageUpdateRequestDto;
import jakarta.validation.Valid;

public interface UpdateMyPageUseCase {

    Long updateProfile(MyPageUpdateRequestDto dto, Long userId);


    Long updatePassword(@Valid MyPagePasswordUpdateRequestDto dto, Long userId);
}
