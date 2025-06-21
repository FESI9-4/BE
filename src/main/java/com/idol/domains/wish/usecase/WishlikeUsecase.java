package com.idol.domains.wish.usecase;

import com.idol.domains.wish.dto.request.WishLikeRequestDto;

public interface WishlikeUsecase {
    void WishLike(WishLikeRequestDto requestDto, Long memberId);
}
