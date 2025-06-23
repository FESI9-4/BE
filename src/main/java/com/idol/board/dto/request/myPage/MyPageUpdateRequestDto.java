package com.idol.board.dto.request.myPage;

public record MyPageUpdateRequestDto(
        String userId,
        String nickName,
        String email,
        String information,
        String profileImgUrl
) {
}
