package com.idol.domains.wish.repository;

import com.idol.domains.wish.domain.Wish;
import com.idol.domains.wish.dto.response.GetMembersWishListResponseDto;

import java.util.List;

public interface WishRepository {
    Wish save(Wish wish);
    void saveAll(List<Wish> wishes);
    void deleteByMemberIdAndArticleId(Long memberId, Long articleId);

    List<GetMembersWishListResponseDto> findByMemberId(Long memberId);
    List<Long> findArticleIdsByMemberId(Long memberId);
    Long countByMemberIdAndIsDeletedFalse(Long memberId);
}
