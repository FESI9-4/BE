package com.idol.board.usecase.mypage;

import com.idol.board.dto.request.myPage.MyPageUpdateRequestDto;

public interface UpdateMyPageUseCase {

    Long updateProfile(MyPageUpdateRequestDto dto, Long userId);
}
