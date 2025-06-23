package com.idol.domains.wish.usecase;

import java.util.List;

public interface GetWishListByMemberIdUsecase {
    List<Long> findArticleIdsByMemberId(Long memberId);
}
