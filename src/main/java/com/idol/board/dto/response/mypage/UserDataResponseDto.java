package com.idol.board.dto.response.mypage;

import com.idol.domains.member.domain.Member;

public record UserDataResponseDto(
        String nickName,
        String profileImg,
        int wishLikeCount,
        String description
) {

    public static UserDataResponseDto from(Member member, String profileImg, int wishLikeCount) {
        return  new UserDataResponseDto(
                member.getNickname(),
                profileImg,
                wishLikeCount,
                member.getInformation()
        );
    }
}
